package com.example.letsplay.ui.common

interface BasePresenter<V> {

    var view: V?
    /**
     * Contains common setup actions needed for all presenters, if any.
     * Subclasses may override this.
     */
    fun start() {}

    /**
     * Contains common cleanup actions needed for all presenters, if any.
     * Subclasses may override this.
     */
    fun stop() {
        view = null
    }
}