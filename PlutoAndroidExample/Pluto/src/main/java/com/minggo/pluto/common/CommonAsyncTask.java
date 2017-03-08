package com.minggo.pluto.common;

import android.os.Handler;
import android.os.Message;
import android.os.Process;

import com.google.gson.Gson;
import com.minggo.pluto.util.LogUtils;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * 异步任务
 *
 * @param <Params>
 * @param <Progress>
 * @param <Result>
 * @author minggo
 * @time 2014-12-29上午10:14:21
 */
public abstract class CommonAsyncTask<Params, Progress, Result> implements Observer {

    private static final String TAG = "CommonAsyncTask";

    private static final int CORE_POOL_SIZE = 5;
    private static final int MAXIMUM_POOL_SIZE = 5;
    private static final int KEEP_ALIVE = 10;

    private static final BlockingQueue<Runnable> sWorkQueue = new ArrayBlockingQueue<Runnable>(120);

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "CommonAsyncTask #" + mCount.getAndIncrement());
        }
    };

    private static final ThreadPoolExecutor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE,
            KEEP_ALIVE, TimeUnit.SECONDS, sWorkQueue, sThreadFactory, new ThreadPoolExecutor.DiscardOldestPolicy());

    private static final int MESSAGE_POST_RESULT = 0x1;
    private static final int MESSAGE_POST_PROGRESS = 0x2;
    private static final int MESSAGE_POST_CANCEL = 0x3;
    /**
     * 同一个UI线程中的消息队列只有在UI线程空闲前不再往消息队列添加消息时队列才开始处理消息
     */
    private static final InternalHandler sHandler = new InternalHandler();

    private final WorkerRunnable<Params, Result> mWorker;
    private final FutureTask<Result> mFuture;

    private volatile Status mStatus = Status.PENDING;

    protected Gson gson = new Gson();

    public enum Status {
        /**
         * 等待运行的线程.
         */
        PENDING,
        /**
         * 运行中的线程.
         */
        RUNNING,
        /**
         * 运行完毕的线程.
         */
        FINISHED,
    }

    /**
     * 创建一个新的线程. 被界面主线程调用.
     */
    public CommonAsyncTask() {
        mWorker = new WorkerRunnable<Params, Result>() {
            public Result call() throws Exception {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                return doInBackground(mParams);
            }
        };
        mFuture = new FutureTask<Result>(mWorker) {
            @Override
            protected void done() {
                Message message;
                Result result = null;

                try {
                    if (isCancelled()) {
                        message = sHandler.obtainMessage(MESSAGE_POST_CANCEL, new UserTaskResult<Result>(
                                CommonAsyncTask.this, (Result[]) null));
                        message.sendToTarget();
                        //System.out.println("CommonAsyncTask cancel----------------------->");
                        return;
                    }
                    result = get();
                } catch (InterruptedException e) {
                    LogUtils.error(TAG, e.toString());
                } catch (ExecutionException e) {
                    throw new RuntimeException("An error occured while executing doInBackground()", e.getCause());
                } catch (CancellationException e) {
                    message = sHandler.obtainMessage(MESSAGE_POST_CANCEL, new UserTaskResult<Result>(
                            CommonAsyncTask.this, (Result[]) null));
                    message.sendToTarget();
                   // System.out.println("CommonAsyncTask cancel----------------------->");
                    return;
                } catch (Throwable t) {
                    throw new RuntimeException("An error occured while executing " + "doInBackground()", t);
                }

                message = sHandler.obtainMessage(MESSAGE_POST_RESULT, new UserTaskResult<Result>(CommonAsyncTask.this,
                        result));
                message.sendToTarget();
            }
        };
    }

    /**
     * 返回当前线程的状态.
     *
     * @return The current status.
     */
    public final Status getStatus() {
        return mStatus;
    }

    /**
     * 此方法在后台线程执行，完成任务的主要工作，通常需要较长的时间。在执行过程中可以调用.
     * 后台进程执行的具体计算在这里实现，doInBackground
     * (Params...)是AsyncTask的关键，此方法必须重载。在这个方法内可以使用
     * publishProgress(Progress...)改变当前的进度值。
     */
    protected abstract Result doInBackground(Params... params);

    /**
     * 执行预处理，它运行于UI线程，可以为后台任务做一些准备工作，比如绘制一个进度条控件。
     *
     * @see #onPostExecute(Object)
     * @see #doInBackground(Object[])
     */
    protected void onPreExecute(Params... params) {
    }

    /**
     * 此方法在主线程执行，任务执行的结果作为此方法的参数返回。
     * 运行于UI线程，可以对后台任务的结果做出处理，结果就是doInBackground(Params
     * ...)的返回值。此方法也要经常重载，如果Result为null表明后台任务没有完成(被取消或者出现异常)。
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void onPostExecute(Result result) {
    }

    /**
     * 运行于UI线程。如果在doInBackground(Params...)中使用了publishProgress(Progress...)，
     * 就会触发这个方法。在这里可以对进度条控件根据进度值做出具体的响应。
     */
    @SuppressWarnings({"UnusedDeclaration"})
    protected void onProgressUpdate(Progress... values) {

    }

    /**
     * 在界面主线程并在cancel(boolean) 之后调用.
     *
     * @see #cancel(boolean)
     * @see #isCancelled()
     */
    protected void onCancelled() {
        AppContext.getInstance().getAsyncTaskManager().deleteObserver(this);
    }

    /**
     * 如果当前任务在正常完成之前被取消返回 true
     *
     * @see #cancel(boolean)
     */
    public final boolean isCancelled() {
        return mFuture.isCancelled();
    }

    /**
     * 如果未开始运行则会被取消运行，如果已经运行则会跑出mayInterruptIfRunning错误并终止运行
     *
     * @param mayInterruptIfRunning 如果为true 如果线程正在执行则会被中断，否则，会等待任务完成。
     * @return false 如果线程无法取消，很可能是因为已经完成 true 则相反
     * @see #isCancelled()
     * @see #onCancelled()
     */
    public final boolean cancel(boolean mayInterruptIfRunning) {

        return mFuture.cancel(mayInterruptIfRunning);
    }

    /**
     * 返回结果
     */
    public final Result get() throws InterruptedException, ExecutionException {
        return mFuture.get();
    }

    /**
     * 在制定时间内等待结果，超出时间则取消任务
     */
    public final Result get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException,
            TimeoutException {
        return mFuture.get(timeout, unit);
    }

    /**
     * 自动调用的回调参数
     *
     * @param params task的需要的参数.
     * @return 返回一个task的实例.
     */
    public final CommonAsyncTask<Params, Progress, Result> execute(Params... params) {
        if (mStatus != Status.PENDING) {
            switch (mStatus) {
                case RUNNING:
                    throw new IllegalStateException("Cannot execute task:" + " the task is already running.");
                case FINISHED:
                    throw new IllegalStateException("Cannot execute task:" + " the task has already been executed "
                            + "(a task can be executed only once)");
            }
        }

        mStatus = Status.RUNNING;

        onPreExecute(params);

        mWorker.mParams = params;
        AppContext.getInstance().getAsyncTaskManager().addTask(this);
        sExecutor.execute(mFuture);
        return this;
    }

    /**
     * 此方法在doInBackground(Object[])里面 当后台计算在运行时在UI线程调用
     * 每次调用都会触发通过onProgressUpdate（）方法在UI线程显示出来
     *
     * @param values UI线程需要的参数
     * @see # onProgressUpdate (Object[])
     * @see #doInBackground(Object[])
     */
    protected final void publishProgress(Progress... values) {
        sHandler.obtainMessage(MESSAGE_POST_PROGRESS, new UserTaskResult<Progress>(this, values)).sendToTarget();
    }

    private void finish(Result result) {

        onPostExecute(result);
        mStatus = Status.FINISHED;
        AppContext.getInstance().getAsyncTaskManager().deleteObserver(this);
    }

    private static class InternalHandler extends Handler {
        @SuppressWarnings({"unchecked", "RawUseOfParameterizedType"})
        @Override
        public void handleMessage(Message msg) {
            UserTaskResult result = (UserTaskResult) msg.obj;
            switch (msg.what) {
                case MESSAGE_POST_RESULT:
                    // 只有一个返回
                    if (result.mTask.isCancelled()) {
                        result.mTask.cancel(true);
                        //System.out.println("CommonAsyncTask cancel----------------------->");
                        break;
                    }
                    result.mTask.finish(result.mData[0]);
                    break;
                case MESSAGE_POST_PROGRESS:
                    result.mTask.onProgressUpdate(result.mData);
                    break;
                case MESSAGE_POST_CANCEL:
                    result.mTask.cancel(true);
                    break;
            }
        }
    }

    private static abstract class WorkerRunnable<Params, Result> implements Callable<Result> {
        Params[] mParams;
    }

    @SuppressWarnings({"RawUseOfParameterizedType"})
    private static class UserTaskResult<Data> {
        final CommonAsyncTask mTask;
        final Data[] mData;

        UserTaskResult(CommonAsyncTask task, Data... data) {
            mTask = task;
            mData = data;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        if (AsyncTaskManager.CANCEL_ALL == (Integer) arg) {
            if (getStatus() == CommonAsyncTask.Status.RUNNING) {
                cancel(true);
            }
        }
    }

}