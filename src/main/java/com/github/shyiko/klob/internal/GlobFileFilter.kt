package com.github.shyiko.klob.internal

import java.io.File
import java.io.FileFilter

internal class GlobFileFilter(baseDir: String, vararg pattern: String) : FileFilter {

    private val glob: Collection<Glob> = pattern.map { Glob(slash(baseDir), slash(it), includeChildren = true) }

    /**
     * @return true if at least one glob explicitly includes file (any matching file excluded by a previous pattern
     * will become included again), false otherwise
     */
    override fun accept(file: File): Boolean {
        return glob.fold(false) { r, g ->
            val absolutePath = slash(file.absolutePath)
            if (g.pattern.startsWith("!")) { return@fold g.matches(absolutePath) && r }
            return@fold g.matches(absolutePath, !file.isDirectory) || r
        }
    }
}
