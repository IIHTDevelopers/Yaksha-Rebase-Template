package mainapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyApp {

    // Method to check if the current branch is 'main' and if there are other branches
    public static String isCurrentBranchMain() {
        try {
            System.out.println("Checking if the current branch is 'main' and if 'feature-branch' exists...");

            // Get the list of branches using 'git branch --list'
            String branches = executeCommand("git branch --list").trim();

            // Pattern to match the current branch (marked with a *) and check if 'main' is the current branch
            Pattern currentBranchPattern = Pattern.compile("\\*\\s*(main)");
            Matcher currentBranchMatcher = currentBranchPattern.matcher(branches);

            // Pattern to check if 'feature-branch' exists in the list of branches
            Pattern featureBranchPattern = Pattern.compile("\\s*(feature-branch)\\s*");
            Matcher featureBranchMatcher = featureBranchPattern.matcher(branches);

            // Check if the current branch is 'main' and if 'feature-branch' exists
            if (currentBranchMatcher.find() && featureBranchMatcher.find()) {
                return "true"; // Current branch is 'main' and 'feature-branch' exists
            } else {
                return "false"; // Either the current branch is not 'main' or 'feature-branch' doesn't exist
            }

        } catch (Exception e) {
            System.out.println("Error in isCurrentBranchMain method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if commits in the current branch match the commits in 'feature-branch'
    public static String compareCommits() {
        try {
            System.out.println("Checking commits of the current branch and 'feature-branch'...");

            // Get the log of the current branch and 'feature-branch' using 'git log'
            String currentBranchLog = executeCommand("git log --oneline --graph --decorate").trim();
            String featureBranchLog = executeCommand("git log feature-branch --oneline --graph --decorate").trim();

            // Split the logs into individual lines
            String[] currentBranchLines = currentBranchLog.split("\n");
            String[] featureBranchLines = featureBranchLog.split("\n");

            // Iterate through each line of the feature-branch log and check if it is present in the current branch log
            for (String featureLine : featureBranchLines) {
                // Pattern to match each line from the feature branch log in the current branch log
                Pattern pattern = Pattern.compile(Pattern.quote(featureLine));
                Matcher matcher = pattern.matcher(currentBranchLog);
                
                // Check if the line from feature-branch is present in current branch's log
                if (!matcher.find()) {
                    return "false"; // If any line is not found, return false
                }
            }

            // If all lines from feature branch are found in the current branch log, return true
            return "true";

        } catch (Exception e) {
            System.out.println("Error in compareCommits method: " + e.getMessage());
            return "";
        }
    }

    // Method to check if there is any merge commit in the reflog
    public static String checkReflogForMerge() {
        try {
            System.out.println("Checking if there is a merge commit in the reflog...");

            // Get the reflog using 'git reflog'
            String reflog = executeCommand("git reflog").trim();

            // Pattern to match any merge commit in the reflog
            Pattern mergePattern = Pattern.compile("merge", Pattern.CASE_INSENSITIVE);
            Matcher mergeMatcher = mergePattern.matcher(reflog);

            // Check if 'merge' is found in the reflog
            if (mergeMatcher.find()) {
                System.out.println("Merge commit found in the reflog.");
                return "false"; // Merge commit found
            }

            System.out.println("No merge commit found in the reflog.");

            // List of required commits to check in the log
            String[] requiredCommits = {
                "Initial commit on main",
                "Add feature work 1",
                "Add feature work 2",
                "Changes made on main",
                "adding all files"
            };

            // Check for each required commit
            for (String commitMessage : requiredCommits) {
                Pattern commitPattern = Pattern.compile(Pattern.quote(commitMessage), Pattern.CASE_INSENSITIVE);
                Matcher commitMatcher = commitPattern.matcher(reflog);

                if (commitMatcher.find()) {
                    System.out.println("Found commit: " + commitMessage);
                } else {
                    System.out.println("Commit not found: " + commitMessage);
                    return "false"; // If any commit is missing, return false
                }
            }

            // If all checks pass
            System.out.println("All required commits found.");
            return "true"; // No merge commit and all required commits are present

        } catch (Exception e) {
            System.out.println("Error in checkReflogForMerge method: " + e.getMessage());
            return "";
        }
    }

    // Helper method to execute git commands
    private static String executeCommand(String command) throws Exception {
        ProcessBuilder processBuilder = new ProcessBuilder();
        processBuilder.directory(new File(".")); // Ensure this is the correct directory where Git repo is located
        processBuilder.command("bash", "-c", command);
        Process process = processBuilder.start();

        StringBuilder output = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }

        int exitVal = process.waitFor();
        if (exitVal == 0) {
            return output.toString();
        } else {
            System.out.println("Command failed with exit code: " + exitVal);
            throw new RuntimeException("Failed to execute command: " + command);
        }
    }

    public static void main(String[] args) {
        try {
            // Checking if the current branch is 'main'
            String currentBranch = isCurrentBranchMain();
            System.out.println("Is the current branch 'main'?: " + currentBranch);

            // Comparing commits of current branch and feature-branch
            String compareCommitsResult = compareCommits();
            System.out.println("Do the commits match? " + compareCommitsResult);

            // Checking if there are any merge commits in the reflog
            String reflogCheck = checkReflogForMerge();
            System.out.println("Is there any merge commit in the reflog? " + reflogCheck);

        } catch (Exception e) {
            System.out.println("Error in main method: " + e.getMessage());
        }
    }
}
