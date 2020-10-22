package stock.master.app.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class fileOperation {

	public static void compressFile(String sourceFile, String targetDir, String zipName) throws Exception {
		Log.debug("+++++");

		File srcFile = new File(sourceFile);
		if (!srcFile.exists()) {
			Log.error("File not exist. name : " + sourceFile);
			return;
		}
		
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(new File(targetDir, zipName));
			zos = new ZipOutputStream(fos);
			
			zos.putNextEntry(new ZipEntry(srcFile.getName()));
			addFileToZip(srcFile, zos);
			zos.closeEntry();
			
			zos.finish();
			zos.close();
			fos.flush();
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				Log.error("Exception: " + e.toString());
			}
		}
		
		Log.debug("-----");
		return;
	}

	public static void compressDirectory(String sourceDir, String targetDir, String zipName) throws Exception {
		Log.debug("+++++");
		
		File srcDir = new File(sourceDir);
		if (!srcDir.exists()) {
			Log.error("Directory not exist. name : " + sourceDir);
			return;
		}
		
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		
		try {
			fos = new FileOutputStream(new File(targetDir, zipName));
			zos = new ZipOutputStream(fos);
			
			addFolderToZip(srcDir, zos, srcDir);
			
			zos.finish();
			zos.close();
			fos.flush();
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				Log.error("Exception: " + e.toString());
			}
		}

		Log.debug("End");
		return ;
	}

	private static void addFileToZip(File srcFile, ZipOutputStream zos) throws Exception{
		Log.debug("Begin");

		byte[] b = new byte[(int) srcFile.length()];

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(srcFile);
			
			int l;
			while ((l = fis.read(b)) != -1) {
				zos.write(b, 0, l);
			}
			
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				Log.error("Exception: " + e.toString());
			}
			
			b = null;
		}

		Log.debug("End");
	}

	private static void addFolderToZip (File srcDir, ZipOutputStream zos, File rootDir) throws Exception {
		Log.debug("Begin");

		try {
			for (File tmp : srcDir.listFiles()) {
				
				String name = getRelativePath(rootDir, tmp);
				if (tmp.isDirectory()) {

					name = name.endsWith("/") ? name : name + "/";
					zos.putNextEntry(new ZipEntry(name));
					addFolderToZip(tmp, zos, rootDir);
				} else {
					zos.putNextEntry(new ZipEntry(name));
					addFileToZip(tmp, zos);
					zos.closeEntry();
				}
			}
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}

		Log.debug("End");
	}

	private static String getRelativePath(File srcDir, File targetFile) {
		
		final int rootLength = srcDir.getAbsolutePath().length();
		final String absFileName = targetFile.getAbsolutePath();
		final String relFileName = absFileName.substring(rootLength + 1);
	    
	    return relFileName;
	}

	// Delete file
	public static void delFile(String filePathAndName) throws Exception {
		try {
			deleteImpl(filePathAndName);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}

	// Delete folder
	public static void delFolder(String folderPath) throws Exception {
		try {
			deleteImpl(folderPath);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}

	private static void deleteImpl (String source) throws Exception {
		Path delPath = Paths.get(source);
		if (!Files.exists(delPath)) {
			Log.error("source not exist : " + source);
			return;
		}

		Files.walk(delPath).sorted(Comparator.reverseOrder()).forEach(path -> {
			try {
				//System.out.println("Deleting: " + path);
				Files.delete(path);
			} catch (Exception e) {
				throw new RuntimeException(Log.error("Exception: " + e.toString()));
			}
		});
	}

	// Copy file
	public static void copyFile(String oldPath, String newPath) throws Exception {
		try {
			copyImpl(oldPath, newPath);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}

	// Copy folder
	public static void copyFolder(String oldPath, String newPath) throws Exception {
		try {
			copyImpl(oldPath, newPath);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}

	private static void copyImpl (String oldPath, String newPath) throws Exception {
		Path sourceDir = Paths.get(oldPath);
        Path destinationDir = Paths.get(newPath);
        if (!Files.exists(sourceDir)) {
        	Log.error("source not exist : " + oldPath);
            return;
        }

        // Traverse the file tree and copy each file/directory.
        Files.walk(sourceDir).forEach(sourcePath -> {
            try {
                Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                //System.out.println("target parent : " + targetPath.getParent());
                if (!Files.exists(targetPath.getParent())) {
                	// create directory
                	Path newDir = Paths.get(targetPath.getParent().toString());
                	Files.createDirectory(newDir);
                }
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
            	throw new RuntimeException(Log.error("Exception: " + e.toString()));
            }
        });
	}

	// Move file
	public static void moveFile(String oldPath, String newPath) throws Exception {
		try {
			copyFile(oldPath, newPath);
			delFile(oldPath);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}

	// Move folder
	public static void moveFolder(String oldPath, String newPath) throws Exception {
		try {
			copyFolder(oldPath, newPath);
			delFolder(oldPath);
		} catch (Exception e) {
			throw new Exception(Log.error("Exception: " + e.toString()));
		}
	}
	
	public static boolean checkExist(String source) {
		Path delPath = Paths.get(source);
		if (!Files.exists(delPath)) {
			return false;
		}
		return true;
	}
}
