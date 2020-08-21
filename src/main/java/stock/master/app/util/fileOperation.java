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


import stock.master.app.service.logService;

public class fileOperation {

	public static void compressFile(String sourceFile, String targetDir, String zipName) throws Exception {
		logService.debug("+++++");

		File srcFile = new File(sourceFile);
		if (!srcFile.exists()) {
			logService.error("File not exist. name : " + sourceFile);
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
			throw new Exception(logService.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				logService.error("Exception: " + e.toString());
			}
		}
		
		logService.debug("-----");
		return;
	}

	public static void compressDirectory(String sourceDir, String targetDir, String zipName) throws Exception {
		logService.debug("+++++");
		
		File srcDir = new File(sourceDir);
		if (!srcDir.exists()) {
			logService.error("Directory not exist. name : " + sourceDir);
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
			throw new Exception(logService.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				logService.error("Exception: " + e.toString());
			}
		}

		logService.debug("End");
		return ;
	}

	private static void addFileToZip(File srcFile, ZipOutputStream zos) throws Exception{
		logService.debug("Begin");

		byte[] b = new byte[(int) srcFile.length()];

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(srcFile);
			
			int l;
			while ((l = fis.read(b)) != -1) {
				zos.write(b, 0, l);
			}
			
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (Exception e) {
				logService.error("Exception: " + e.toString());
			}
			
			b = null;
		}

		logService.debug("End");
	}

	private static void addFolderToZip (File srcDir, ZipOutputStream zos, File rootDir) throws Exception {
		logService.debug("Begin");

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
			throw new Exception(logService.error("Exception: " + e.toString()));
		}

		logService.debug("End");
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
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}

	// Delete folder
	public static void delFolder(String folderPath) throws Exception {
		try {
			deleteImpl(folderPath);
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}

	private static void deleteImpl (String source) throws Exception {
		Path delPath = Paths.get(source);

		Files.walk(delPath).sorted(Comparator.reverseOrder()).forEach(path -> {
			try {
				//System.out.println("Deleting: " + path);
				Files.delete(path);
			} catch (Exception e) {
				throw new RuntimeException(logService.error("Exception: " + e.toString()));
			}
		});
	}

	// Copy file
	public static void copyFile(String oldPath, String newPath) throws Exception {
		try {
			copyImpl(oldPath, newPath);
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}

	// Copy folder
	public static void copyFolder(String oldPath, String newPath) throws Exception {
		try {
			copyImpl(oldPath, newPath);
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}

	private static void copyImpl (String oldPath, String newPath) throws Exception {
		Path sourceDir = Paths.get(oldPath);
        Path destinationDir = Paths.get(newPath);

        // Traverse the file tree and copy each file/directory.
        Files.walk(sourceDir).forEach(sourcePath -> {
            try {
                Path targetPath = destinationDir.resolve(sourceDir.relativize(sourcePath));
                //System.out.printf("Copying %s to %s%n", sourcePath, targetPath);
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception e) {
            	throw new RuntimeException(logService.error("Exception: " + e.toString()));
            }
        });
	}

	// Move file
	public static void moveFile(String oldPath, String newPath) throws Exception {
		try {
			copyFile(oldPath, newPath);
			delFile(oldPath);
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}

	// Move folder
	public static void moveFolder(String oldPath, String newPath) throws Exception {
		try {
			copyFolder(oldPath, newPath);
			delFolder(oldPath);
		} catch (Exception e) {
			throw new Exception(logService.error("Exception: " + e.toString()));
		}
	}
}
