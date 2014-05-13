/**
 * Copyright (c) 2012 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.xtext.builddsl.lib;

import com.google.common.io.Files;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@SuppressWarnings("all")
public class FileExtensions {
  private static void collectFiles(final File root, final List<File> result) {
    File[] _listFiles = root.listFiles();
    for (final File it : _listFiles) {
      boolean _isFile = it.isFile();
      if (_isFile) {
        result.add(it);
      } else {
        boolean _isDirectory = it.isDirectory();
        if (_isDirectory) {
          FileExtensions.collectFiles(it, result);
        }
      }
    }
  }
  
  public static ArrayList<File> listAllFiles(final File file) {
    ArrayList<File> _xblockexpression = null;
    {
      final ArrayList<File> files = CollectionLiterals.<File>newArrayList();
      FileExtensions.collectFiles(file, files);
      _xblockexpression = files;
    }
    return _xblockexpression;
  }
  
  public static void zip(final File directory, final File zipFile) {
    try {
      final ArrayList<File> files = FileExtensions.listAllFiles(directory);
      FileOutputStream _fileOutputStream = new FileOutputStream(zipFile);
      BufferedOutputStream _bufferedOutputStream = new BufferedOutputStream(_fileOutputStream);
      final JarOutputStream out = new JarOutputStream(_bufferedOutputStream);
      try {
        out.setMethod(JarOutputStream.DEFLATED);
        final Procedure1<File> _function = new Procedure1<File>() {
          public void apply(final File file) {
            try {
              File _relativeTo = FileExtensions.relativeTo(file, directory);
              String _path = _relativeTo.getPath();
              final ZipEntry entry = new ZipEntry(_path);
              out.putNextEntry(entry);
              Files.copy(file, out);
              out.closeEntry();
            } catch (Throwable _e) {
              throw Exceptions.sneakyThrow(_e);
            }
          }
        };
        IterableExtensions.<File>forEach(files, _function);
        out.flush();
      } finally {
        out.close();
      }
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  public static File operator_divide(final File dir, final String name) {
    File _xblockexpression = null;
    {
      boolean _isDirectory = dir.isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        throw new IllegalStateException(("This is not a directory: " + dir));
      }
      URI _uRI = dir.toURI();
      URI _resolve = _uRI.resolve(name);
      _xblockexpression = new File(_resolve);
    }
    return _xblockexpression;
  }
  
  public static File operator_divide(final String dir, final String post) {
    File _xblockexpression = null;
    {
      File _file = FileExtensions.file(dir);
      boolean _isDirectory = _file.isDirectory();
      boolean _not = (!_isDirectory);
      if (_not) {
        throw new IllegalStateException(("This is not a directory: " + dir));
      }
      File _file_1 = FileExtensions.file(dir);
      _xblockexpression = new File(_file_1, post);
    }
    return _xblockexpression;
  }
  
  public static File file(final String path) {
    return new File(path);
  }
  
  public static File relativeTo(final File target, final File base) {
    File _absoluteFile = base.getAbsoluteFile();
    URI _uRI = _absoluteFile.toURI();
    URI _uRI_1 = target.toURI();
    URI _relativize = _uRI.relativize(_uRI_1);
    String _string = _relativize.toString();
    return new File(_string);
  }
}
