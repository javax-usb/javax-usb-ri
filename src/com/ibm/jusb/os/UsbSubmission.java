package com.ibm.jusb.os;

/**
 * Copyright (c) 1999 - 2001, International Business Machines Corporation.
 * All Rights Reserved.
 *
 * This software is provided and licensed under the terms and conditions
 * of the Common Public License:
 * http://oss.software.ibm.com/developerworks/opensource/license-cpl.html
 */

import javax.usb.*;

/**
 * Interface representing a single submission.
 * @author Dan Streetman
 */
public interface UsbSubmission
{
	/**
	 * Get the data to transfer.
	 * @return The data to transfer.
	 */
	public byte[] getData();

	/**
	 * Get the offset of the data.
	 * <p>
	 * The data to transfer starts at this point in the {@link #getData() buffer}.
	 * @return The offset of the data.
	 */
	public int getOffset();

	/**
	 * Get the amount of data to transfer.
	 * <p>
	 * This will never be more than the length of the buffer.
	 * @return The amount of data to transfer.
	 */
	public int getLength();

	/**
	 * Get the amount of data actually transferred.
	 * <p>
	 * This will never be more than the {@link #getLength length}.
	 * @return The amount of data actually transferred.
	 */
	public int getActualLength();

	/**
	 * Set the amount of data actually transferred.
	 * <p>
	 * This cannot be more than the {@link #getLength() length}.
	 * @param len The amount of data actually transferred.
	 */
	public void setActualLength(int len);

	/**
	 * Get the UsbException.
	 * @return The UsbException, or null if none occurred.
	 */
	public UsbException getUsbException();

	/**
	 * Set the UsbException.
	 * @param uE The UsbException.
	 */
	public void setUsbException(UsbException uE);

	/**
	 * Complete this submission.
	 */
	public void complete();

}
