package org.vosk

/**
 * Batch model object
 *
 * 26 / 12 / 2022
 *
 * @constructor Creates the batch recognizer object
 */
actual class BatchModel : Freeable {

	actual constructor(path: String)

	/**
	 *  Releases batch model object
	 */
	actual override fun free() {
	}

	/**
	 * Wait for the processing
	 */
	actual fun await() {

	}

}