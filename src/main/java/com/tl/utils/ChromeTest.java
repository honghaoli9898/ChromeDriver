package com.tl.utils;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ChromeTest {

	public static void main(String[] args) {
		String url = "https://passport.jd.com/uc/login?ltype=logout";
		String jdSearchUrl = "https://search.jd.com/Search?keyword=手机&enc=utf-8&qrst=1&rt=1&stop=1&vt=2&cid2=653&cid3=655&page=1&s=1&click=0";
		;
		String fileName = jdSearchUrl.hashCode() + ".txt";
		String fileName_1 = url.hashCode() + ".txt";
		WebDriver webDriver = null;
		try {
			webDriver = WebDriverUtil.createChromeWebDriver(
					"plugins\\chromedriver\\chromedriver.exe", false);
			webDriver.get(url);
			System.out.println("即将点击账户登陆");
			webDriver
					.findElement(
							By.xpath("//*[@id=\"content\"]/div[2]/div[1]/div/div[3]/a"))
					.click();
			System.out.println("即将睡眠1秒");
			Thread.sleep(1000);
			WebElement username = webDriver.findElement(By
					.xpath("//*[@id=\"loginname\"]"));
			WebElement password = webDriver.findElement(By
					.xpath("//*[@id=\"nloginpwd\"]"));
			WebElement submit = webDriver.findElement(By
					.xpath("//*[@id=\"loginsubmit\"]"));
			System.out.println("输入账号和密码");
			username.sendKeys("15803289100");
			password.sendKeys("haojiayou@12345");
			submit.click();
			String title = webDriver.getTitle();
			while (!title.equals("京东(JD.COM)-正品低价、品质保障、配送及时、轻松购物！")) {
				title = webDriver.getTitle();
				Thread.sleep(1000);
			}
			System.out.println("登录成功");
			webDriver.get(jdSearchUrl);
			// 下拉到页面底部
			((JavascriptExecutor) webDriver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight/2)");
			Thread.sleep(5000);
			((JavascriptExecutor) webDriver)
					.executeScript("window.scrollTo(document.body.scrollHeight/2,document.body.scrollHeight)");
			Thread.sleep(3000);
			String html = webDriver.getPageSource();
			IOUtil.writeFile(html, fileName);
			// ((JavascriptExecutor)webDriver).executeScript("window.scrollTo(document.body.scrollHeight,document.body.scrollHeight*0.75)");
			List<WebElement> webElementList = webDriver
					.findElements(By
							.cssSelector("#J_goodsList > ul > li > div > div.p-img > a"));
			System.out.println(webElementList.size()); // #J_goodsList > ul >
			for (WebElement webElement : webElementList) {
				webElement.click();
				break;
			}
			Thread.sleep(5000);
			((JavascriptExecutor) webDriver)
					.executeScript("window.scrollTo(0,document.body.scrollHeight)");
			Thread.sleep(5000);
			// webDriver.findElement(By.cssSelector("#detail > div.tab-main.large > ul > li:nth-child(5)")).click();
			webDriver
					.get("https://sclub.jd.com/comment/productPageComments.action?&productId=100001172674&score=0&sortType=5&page=0&pageSize=10");
			Thread.sleep(1000);
			html = webDriver.getPageSource();
			html = JsoupUtil.getElementsBySelector(html, "body").outerHtml();
			IOUtil.writeFile(html, fileName_1);
			// 下拉到页面底部
			// ((JavascriptExecutor)webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
			// 上拉到页面顶端
			// ((JavascriptExecutor)webDriver).executeScript("window.scrollTo(document.body.scrollHeight,0)");
			// String html = webDriver.getPageSource();
			// IOUtil.writeFile(html, fileName);
			// System.out.println(html);
			// 下拉到页面1000位置
			// ((JavascriptExecutor)
			// webDriver).executeScript("window.scrollTo(0,1000)");
			// 上拉到页面顶端 0,0位置
			// ((JavascriptExecutor)
			// webDriver).executeScript("window.scrollTo(0,0)");
		} catch (Exception e) {
			e.printStackTrace();
		}
		// finally {
		// if (webDriver != null) {
		// webDriver.close();
		// }
		// }
	}
}
