package com.relevantcodes.extentreports.view.extenthtml;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.Base;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.reporter.ExtentHtmlReporter;
import com.relevantcodes.extentreports.utils.Reader;

public class TestAndNodeAttributesTest extends Base {
    
    final String filePath = getOutputFolder() + getClass().getName() + ".html";
    final String testName = getClass().getName();
    
    ExtentReports extent;
    ExtentTest test;
    ExtentTest node;
    Elements htmlTest;
    Elements htmlNode;

    @BeforeClass
    public void setup() {
        ExtentHtmlReporter htmlReporter = new ExtentHtmlReporter(filePath);
        
        extent = new ExtentReports();
        extent.attachReporter(htmlReporter);
        
        test = extent.createTest(testName);
        node = test.createNode(testName + ".Node");
        node.pass(testName + ".Node");
        
        extent.flush();
        
        String html = Reader.readAllText(filePath);
        Document doc = Jsoup.parse(html);
        
        htmlTest = doc.select("#test-collection .test");
        htmlNode = htmlTest.select(".node");
    }

    @Test
    public void validatesTestsCountView() {        
        int testCount = htmlTest.size();       
        Assert.assertEquals(1, testCount);
    }
    
    @Test
    public void validatesNodesCountView() {
        int nodeCount = htmlNode.size();
        Assert.assertEquals(test.getModel().getNodeContext().getAll().size(), nodeCount);
    }

    @Test
    public void validatesTestIDView() {
        String testId = htmlTest.attr("test-id");
        Assert.assertEquals(String.valueOf(test.getModel().getID()), testId);
    }
    
    @Test
    public void validatesNodeIDView() {
        String nodeId = htmlNode.first().attr("test-id");
        Assert.assertEquals(String.valueOf(test.getModel().getNodeContext().get(0).getID()), nodeId);
    }
    
    @Test
    public void validatesTestStatusView() {
        String status = htmlTest.attr("status");
        Assert.assertEquals(String.valueOf(test.getStatus()).toLowerCase(), status);
    }
    
    @Test
    public void validatesNodeStatusView() {
        String status = htmlNode.first().attr("status");
        Assert.assertEquals(String.valueOf(test.getModel().getNodeContext().get(0).getStatus()).toLowerCase(), status);
    }
    
    @Test
    public void validatesIfBdd() {
        boolean isBdd = Boolean.valueOf(htmlTest.attr("bdd"));
        Assert.assertEquals(false, isBdd);
    }
    
    @Test
    public void validatesTestNameView() {
        String testName = htmlTest.select(".test-name").first().html();
        Assert.assertEquals(this.testName, testName);
    }
    
    @Test
    public void validatesNodeNameView() {
        String nodeName = htmlNode.first().select(".node-name").first().html();
        Assert.assertEquals(test.getModel().getNodeContext().get(0).getName(), nodeName);
    }
    
    @Test
    public void validatesTestLogSizeView() {
        Elements log = htmlTest.select(".test-content > .test-steps .log");
        int logSize = log.size();
        Assert.assertEquals(0, logSize);
    }
    
    @Test
    public void validatesNodeLogSizeView() {
        Elements log = htmlNode.select(".log");
        int logSize = log.size();
        Assert.assertEquals(test.getModel().getNodeContext().get(0).getLogContext().getAll().size(), logSize);
    }
}
