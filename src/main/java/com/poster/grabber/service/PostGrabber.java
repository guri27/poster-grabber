/**
  PostGrabber.java
 ***********************************************************************************************************************
 Description: 	

 Revision History:
 -----------------------------------------------------------------------------------------------------------------------
 Date         	Author               	Reason for Change
 -----------------------------------------------------------------------------------------------------------------------
 01-Aug-2017	Gurpreet Singh Saini	Initial Version

 ***********************************************************************************************************************
 */
package com.poster.grabber.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;

import org.apache.commons.io.FileExistsException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.poster.grabber.model.MovieModel;
import com.poster.grabber.model.Result;

/**
 *
 */
public class PostGrabber {

	private String directory;

	public PostGrabber(String directory) {
		this.directory = directory;
	}

	private static final String FOLDER_THUMBNAIL = "Folder.jpg";
	private static final String YEAR_PREFIX = "(";
	private static final String YEAR_POSTFIX = ")";
	private static final String API_KEY = "682695192e252905a45c46ff9e0011a5";
	private static final String SEARCH_URL = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&query=";
	private static final String POSTER_URL = "http://image.tmdb.org/t/p/w500";
	private static final String FILTER_NAME_REGEX = "[\\d\\'(']";

	/**
	 * This method gets all files of the directory
	 * 
	 * @return
	 * @throws IOException
	 */
	private DirectoryStream<Path> getFiles() throws IOException {
		Path path = Paths.get(this.directory);
		DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);
		return dirStream;
	}

	/**
	 * This method verifies, if thumbnail already exists
	 * 
	 * @param path
	 * @return
	 */
	private boolean isThumbnailExist(Path path) {
		DirectoryStream<Path> dirStream;
		try {
			dirStream = Files.newDirectoryStream(path);
			for (Path subPath : dirStream) {
				if (subPath.getFileName().toString().equalsIgnoreCase(FOLDER_THUMBNAIL)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println("Error while getting thumbnail for: " + path);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * This method remove extension name from file
	 * 
	 * @param name
	 * @return
	 */
	private String filterExt(String name) {
		String filteredName = null;
		filteredName = FilenameUtils.removeExtension(name);
		return filteredName;
	}

	/**
	 * This method filters name, so that it can be searched
	 * 
	 * @param name
	 * @return
	 */
	private String filterName(String name) {
		String filteredName = null;
		filteredName = name.replace(".", " ");
		filteredName = filteredName.split(FILTER_NAME_REGEX)[0];
		return filteredName;
	}

	/**
	 * This method search the movie name and gets details
	 * 
	 * @param searchName
	 * @return
	 */
	private MovieModel searchName(String searchName) {
		String name = filterName(searchName);
		System.out.println("Getting details for: " + name);
		MovieModel res = null;
		try {
			RestTemplate getRequest = new RestTemplate();
			ResponseEntity<Result> response = getRequest.getForEntity(SEARCH_URL + name, Result.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				Result result = response.getBody();
				if (result != null && !result.getResults().isEmpty()) {
					res = result.getResults().get(0);
				}
			}
		} catch (Exception e) {
			System.out.println("Exception while doing operation");
			e.printStackTrace();
		}
		return res;
	}

	/**
	 * This method downloads poster
	 * 
	 * @param movie
	 * @param path
	 */
	private void downloadPoster(MovieModel movie, Path path) {
		try {
			System.out.println("Downloading poster for:" + movie.getOriginal_title());
			RestTemplate down = new RestTemplate();
			byte[] imageBytes = down.getForObject(POSTER_URL + movie.getPoster_path(), byte[].class);
			Files.write(Paths.get(path.toAbsolutePath().toString() + File.separator + FOLDER_THUMBNAIL), imageBytes);
			System.out.println("Successfully created image for: " + path.getFileName());
		} catch (IOException e) {
			System.out.println("Exception while doing operation");
			e.printStackTrace();
		}
	}

	/**
	 * This method renames the diretory
	 * 
	 * @param movie
	 * @param sourcePath
	 */
	private void renameDir(MovieModel movie, Path sourcePath) {
		Path destPath = null;
		try {
			System.out.println("Renaming directory: " + movie.getOriginal_title());
			// Update dir name, if contains /,?,:
			String updateMovieName = movie.getOriginal_title().replaceAll(":", "");
			movie.setOriginal_title(updateMovieName);
			Calendar cal = Calendar.getInstance();
			cal.setTime(movie.getRelease_date());
			String dest = this.directory + File.separator + movie.getOriginal_title() + " " + YEAR_PREFIX
					+ cal.get(Calendar.YEAR) + YEAR_POSTFIX;
			destPath = Paths.get(dest);
			FileUtils.moveDirectory(sourcePath.toFile(), destPath.toFile());
			System.out.println("Successfully renamed to: " + dest);
		} catch (FileExistsException e) {
			System.out.println("Successfully renamed to: " + destPath);
		} catch (Exception e) {
			System.out.println("Exception while doing operation");
			e.printStackTrace();
		}
	}

	/**
	 * This method creates directory and moves the file into it
	 * 
	 * @param movie
	 * @param sourceFile
	 */
	private void createDirAndMove(MovieModel movie, Path sourceFile) {
		System.out.println("Creating directory for : " + movie.getOriginal_title());

		// Update dir name, if contains /,?,:
		String updateMovieName = movie.getOriginal_title().replaceAll(":", "");
		movie.setOriginal_title(updateMovieName);

		Calendar cal = Calendar.getInstance();
		cal.setTime(movie.getRelease_date());
		Path dirPath = Paths.get(this.directory + File.separator + movie.getOriginal_title() + " " + YEAR_PREFIX
				+ cal.get(Calendar.YEAR) + YEAR_POSTFIX);
		Path path = Paths.get(this.directory + File.separator + movie.getOriginal_title() + " " + YEAR_PREFIX
				+ cal.get(Calendar.YEAR) + YEAR_POSTFIX + File.separator + sourceFile.getFileName());
		try {
			Files.createDirectory(dirPath);
			downloadPoster(movie, dirPath);
			// Move file
			FileUtils.moveFile(sourceFile.toFile(), path.toFile());

		} catch (IOException e) {
			System.out.println("Exception while doing operation");
			e.printStackTrace();
		}
	}

	/**
	 * This method updates the selected directory with thumbnails
	 * 
	 * @throws IOException
	 */
	public void update() throws IOException {
		System.out.println("Grabbing posters in directory: " + this.directory);
		DirectoryStream<Path> dirStream = getFiles();
		dirStream.forEach(dir -> {
			if (Files.isDirectory(dir)) {
				// Check for existing thumbnail
				if (!isThumbnailExist(dir)) {
					// Get details
					MovieModel movie = searchName(dir.getFileName().toString());
					if (movie != null && movie.getPoster_path() != null) {
						downloadPoster(movie, dir);
						renameDir(movie, dir);
					} else {
						System.out.println("Couldn't find details for " + dir.getFileName().toString());
					}
				} else {
					System.out.println("Thumbnail already exist for: " + dir.getFileName().toString());
				}
			} else {
				String name = filterExt(dir.getFileName().toString());
				String filteredName = filterName(name);
				MovieModel movie = searchName(filteredName);
				if (movie != null && movie.getPoster_path() != null) {
					createDirAndMove(movie, dir);
				} else {
					System.out.println("Couldn't find details for " + dir.getFileName().toString());
				}
			}
		});
	}

	/**
	 * This method deletes all the thumbnails existing in that directory
	 * 
	 * @throws IOException
	 */
	public void delete() throws IOException {
		System.out.println("Grabbing posters in directory: " + this.directory);
		DirectoryStream<Path> dirStream = getFiles();
		dirStream.forEach(dir -> {
			if (Files.isDirectory(dir)) {
				if (isThumbnailExist(dir)) {
					Path path = Paths.get(dir.toAbsolutePath().toString() + File.separator + FOLDER_THUMBNAIL);
					try {
						Files.delete(path);
						System.out
								.println("Successfully deleted poster of: " + filterName(dir.getFileName().toString()));
					} catch (IOException e) {
						System.out.println("Unable to delete poster of: " + filterName(dir.getFileName().toString()));
						e.printStackTrace();
					}
				}
			}
		});
	}
	
	/*public static void main(String[] args) {
		PostGrabber postGrabber = new PostGrabber("G:\\Movies\\Angreji");
		try {
			postGrabber.update();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
}
