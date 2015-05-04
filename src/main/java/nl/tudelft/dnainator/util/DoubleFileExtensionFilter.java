/*
 * Copyright (c) 2006, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package nl.tudelft.dnainator.util;

import java.io.File;
import java.util.Locale;

import javax.swing.filechooser.FileFilter;

/**
 * A re-implementation of {@code FileNameExtensionFilter} that filters
 * using a specified set of double extensions. A double extension is an
 * extension from an extension, e.g. tar.gz. This double extension for a
 * file is the portion of the file name after the second last ".".
 * Files whose name does not contain any or exactly one "." have no double
 * file name extension. File name extension comparisons are case insensitive.
 * <p>
 * The following example creates a
 * {@code DoubleFileExtensionFilter} that will show {@code tar.gz} files:
 * <pre>
 * FileFilter filter = new DoubleFileExtensionFilter("TAR gz file", "tar.gz");
 * JFileChooser fileChooser = ...;
 * fileChooser.addChoosableFileFilter(filter);
 * </pre>
 *
 * @see FileFilter
 * @see javax.swing.filechooser.FileNameExtensionFilter;
 * @see javax.swing.JFileChooser#setFileFilter
 * @see javax.swing.JFileChooser#addChoosableFileFilter
 * @see javax.swing.JFileChooser#getFileFilter
 */
public final class DoubleFileExtensionFilter extends FileFilter {
	private final String description;
	private final String[] extensions;
	private final String[] lowerCaseExtensions;

	/**
	 * Creates a {@code DoubleFileExtensionFilter} with the specified
	 * description and double file name extensions. The returned {@code
	 * DoubleFileExtensionFilter} will accept all directories and any
	 * file with a file name extension contained in {@code extensions}.
	 *
	 * @param description textual description for the filter, may be
	 *			  {@code null}
	 * @param extensions the accepted double file name extensions
	 * @throws IllegalArgumentException if extensions is {@code null}, empty,
	 *         contains {@code null}, contains an empty string or is a single
	 *         extension.
	 * @see #accept
	 */
	public DoubleFileExtensionFilter(String description, String... extensions) {
		if (extensions == null || extensions.length == 0) {
			throw new IllegalArgumentException(
				"Extensions must be non-null and not empty");
		}
		this.description = description;
		this.extensions = new String[extensions.length];
		this.lowerCaseExtensions = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			if (extensions[i] == null || extensions[i].length() == 0
					|| extensions[i].lastIndexOf('.') == -1) {
			throw new IllegalArgumentException(
				"Each extension must be non-null, not empty and double");
			}
			this.extensions[i] = extensions[i];
			this.lowerCaseExtensions[i] = extensions[i].toLowerCase(Locale.ENGLISH);
		}
	}

	/**
	 * Tests the specified file, returning true if the file is
	 * accepted, false otherwise. True is returned if the extension
	 * matches one of the double file name extensions of this {@code
	 * FileFilter}, or the file is a directory.
	 *
	 * @param f the {@code File} to test
	 * @return true if the file is to be accepted, false otherwise
	 */
	public boolean accept(File f) {
		if (f == null) {
			return false;
		} else if (f.isDirectory()) {
			return true;
		}

		// NOTE: we tested implementations using Maps, binary search
		// on a sorted list and this implementation. All implementations
		// provided roughly the same speed, most likely because of
		// overhead associated with java.io.File. Therefore we've stuck
		// with the simple lightweight approach.
		String fileName = f.getName();
		int i = fileName.lastIndexOf('.');
		if (i > 0 && i < fileName.length() - 1) {
			int j = fileName.substring(0, i).lastIndexOf('.');
			if (j > 0 && j < fileName.length() - 1) {
				String desiredExtension = fileName.substring(j + 1).
						toLowerCase(Locale.ENGLISH);
				for (String extension : lowerCaseExtensions) {
					if (desiredExtension.equals(extension)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * The description of this filter. For example: "TAR gz file."
	 *
	 * @return the description of this filter
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return The set of double file name extensions files are tested against
	 */
	public String[] getExtensions() {
		String[] result = new String[extensions.length];
		System.arraycopy(extensions, 0, result, 0, extensions.length);
		return result;
	}
}
