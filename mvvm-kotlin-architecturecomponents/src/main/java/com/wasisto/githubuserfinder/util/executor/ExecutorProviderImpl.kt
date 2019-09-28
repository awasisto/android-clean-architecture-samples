/*
 * Copyright (c) 2019 Andika Wasisto
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.wasisto.githubuserfinder.util.executor

import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

object ExecutorProviderImpl : ExecutorProvider {

    private val computationExecutor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    private val ioExecutor = Executors.newCachedThreadPool()

    private val uiExecutor = object : AbstractExecutorService() {
        private val handler: Handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            if (handler.looper == Looper.myLooper()) {
                command.run()
            } else {
                handler.post(command)
            }
        }
        override fun shutdown() = throw UnsupportedOperationException()
        override fun shutdownNow() = throw UnsupportedOperationException()
        override fun isShutdown() = false
        override fun isTerminated() = false
        override fun awaitTermination(timeout: Long, timeUnit: TimeUnit) = throw UnsupportedOperationException()
    }

    override fun computation(): ExecutorService = computationExecutor

    override fun io(): ExecutorService = ioExecutor

    override fun ui(): ExecutorService = uiExecutor
}
