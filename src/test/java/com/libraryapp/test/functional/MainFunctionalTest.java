package com.libraryapp.test.functional;

import static com.libraryapp.test.utils.TestUtils.businessTestFile;
import static com.libraryapp.test.utils.TestUtils.currentTest;
import static com.libraryapp.test.utils.TestUtils.testReport;
import static com.libraryapp.test.utils.TestUtils.yakshaAssert;

import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Order;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import mainapp.MyApp;

public class MainFunctionalTest {

    @AfterAll
    public static void afterAll() {
        testReport();
    }

    @Test
    @Order(1)
    public void testCurrentBranchIsMain() throws IOException {
        try {
            // Check if the current branch is 'main'
            String result = MyApp.isCurrentBranchMain();

            // Assert that the result is 'true' indicating we are on 'main'
            yakshaAssert(currentTest(), result.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(2)
    public void testCompareCommits() throws IOException {
        try {
            // Compare commits of current branch and feature-branch
            String result = MyApp.compareCommits();

            // Assert that the result is 'true' indicating the commits match
            yakshaAssert(currentTest(), result.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }

    @Test
    @Order(3)
    public void testReflogForMerge() throws IOException {
        try {
            // Check if there are any merge commits in the reflog
            String result = MyApp.checkReflogForMerge();

            // Assert that the result is 'true' indicating no merge commit
            yakshaAssert(currentTest(), result.equals("true"), businessTestFile);
        } catch (Exception ex) {
            yakshaAssert(currentTest(), false, businessTestFile);
        }
    }
}
