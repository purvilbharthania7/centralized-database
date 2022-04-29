package User;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import fileUtilities.FileUtility;

public class Login {

	public boolean login(String path, String username, String password, String answer) {
		FileUtility fileUtil = new FileUtility();
		UserRegistration userRegistered = new UserRegistration();
		List<String> columns = fileUtil.getTableColumnsInOrder(path, "user_profile");
		boolean isLoggedIn = false;
		// 0th index is for username, 1 for password, 3 for security answer
		try {
			Scanner sc = new Scanner(new File(path + "\\user_profile.txt"));
			sc.next();
			while(sc.hasNext()) {
				String line = sc.next();
				String[] lineArr = line.split("\\|");
				if(lineArr[0].equals(username)) {
					String hashPassword = userRegistered.encryptPassword(password);
					if(lineArr[1].equals(hashPassword) && lineArr[3].equals(answer)) {
						System.out.println("### Welcome to the database ###");
						isLoggedIn = true;
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		return isLoggedIn;
	}
}
