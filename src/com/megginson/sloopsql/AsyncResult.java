package com.megginson.sloopsql;

/**
 * Class to hold result of an AsyncTask
 *
 * If an {@link AsyncTask} throws an exception or error, there needs to be a way
 * to propagate that information to the main thread.  This class creates a strongly-
 * typed generic object to hold au unexpected {@link Throwable} or the expected result type.
 */
public class AsyncResult<T>
{

	private T mResult;

	private Throwable mThrowable;

	/**
	 * Constructor for the expected result.
	 */
	public AsyncResult(T result)
	{
		mResult = result;
	}

	/**
	 * Constructor for an unexpected throwable.
	 */
	public AsyncResult(Throwable throwable)
	{
		mThrowable = throwable;
	}
	
	/**
	 * Get the expected result.
	 *
	 * @return A result of the expected type, or null if not available.
	 */
	public T getResult()
	{
		return mResult;
	}

	/**
	 * Return the unexpected throwable.
	 *
	 * @return A {@link Throwable} representing an error.
	 */
	public Throwable getThrowable()
	{
		return mThrowable;
	}

	/**
	 * Test if there's an error.
	 *
	 * @return true if {#getThrowable()} returns non-null.
	 */
	public boolean isError()
	{
		return (getThrowable() != null);
	}

}
