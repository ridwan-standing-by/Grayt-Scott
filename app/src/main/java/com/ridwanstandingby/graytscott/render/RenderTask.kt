package com.ridwanstandingby.graytscott.render

interface RenderTask : Runnable {

    var canDraw: Boolean

    override fun run()
}
