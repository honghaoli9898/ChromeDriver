package com.tl.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SiderTest {

	private static String basePath = "plugins/images/";
	private static String GAP_IMAGE_NAME = "full-image";
	public static String path = "plugins/images";
	private static String INDEX_URL = "https://passport.jd.com/uc/login?ltype=logout";// 测试京东登录验证码
	private static WebDriver driver;
	private static int testTimes = 100; // 测试100次，成功率在80%左右
	private static int successTimes = 0;
	public static List<BufferedImage> localImageList;
	static {
		System.setProperty("webdriver.chrome.driver",
				"plugins\\chromedriver\\chromedriver.exe");
		driver = new ChromeDriver();
		try {
			localImageList = getLocalALLImage(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static BufferedImage getSimilarImage(BufferedImage image)
			throws IOException {
		for (BufferedImage bufferedImage : localImageList) {
			int similarNum = compareImage(image, bufferedImage);
			if (similarNum == 100) {
				System.out.println("找到相似度为100的图片");
				return bufferedImage;
			}
		}
		throw new RuntimeException("未找到相似图片");
	}

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < testTimes; i++) { // test 10 times
			try {
				invoke();
				System.out.println("测试" + (i + 1) + "次，成功" + successTimes
						+ "次，成功率为："
						+ (((double) successTimes) / (double) (i + 1)) * 100
						+ "%");
			} catch (IOException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (StaleElementReferenceException e) {
				e.printStackTrace();
			}
		}
		System.out.println("Finally,测试" + testTimes + "次，成功" + successTimes
				+ "次，成功率为：" + (((double) successTimes) / (double) testTimes)
				* 100 + "%");
		driver.quit();
	}

	private static void invoke() throws IOException, InterruptedException,
			StaleElementReferenceException {
		// 设置input参数
		driver.get(INDEX_URL);
		Thread.sleep(2000);
		System.out.println("寻找注册");
		// //*[@id="content"]/div[2]/div[1]/div/div[3]/a
		driver.findElement(
				By.xpath("//*[@id=\"content\"]/div[2]/div[1]/div/div[3]/a"))
				.click();
		Thread.sleep(1000);
		WebElement username = driver.findElement(By
				.xpath("//*[@id=\"loginname\"]"));
		WebElement password = driver.findElement(By
				.xpath("//*[@id=\"nloginpwd\"]"));
		WebElement submit = driver.findElement(By
				.xpath("//*[@id=\"loginsubmit\"]"));
		username.sendKeys("15803289100");
		password.sendKeys("haojiayou@12345");
		submit.click();
		System.out.println("即将睡5秒");
		Thread.sleep(5000);
		By moveBtn = By.cssSelector(".JDJRV-slide-inner.JDJRV-slide-btn");// 滑动验证按钮
		waitForLoad(driver, moveBtn);
		WebElement moveElemet = driver.findElement(moveBtn);
		BufferedImage gapImage = getGapImage(driver);
		BufferedImage similarImage = getSimilarImage(gapImage);
		int distance = getSliceOffset(gapImage, similarImage);
		distance = (int) (distance * 0.77222);
		System.out.println("得到移动距离" + distance * 0.77222);
		move(driver, moveElemet, distance - 1);
		Thread.sleep(4000);
	}

	/**
	 * 移动
	 *
	 * @param driver
	 * @param element
	 * @param distance
	 * @throws InterruptedException
	 */
	public static void move(WebDriver driver, WebElement element, int distance)
			throws InterruptedException {
		// int xDis = distance;// distance to move
//		int moveX = new Random().nextInt(10) - 5;
		int moveY = 1;
		List<Integer> moveArray = GetSpeedArray(distance);
		Actions actions = new Actions(driver);
		new Actions(driver).clickAndHold(element).perform();// click and hold
		Thread.sleep(2000);// slow down
		for (Integer integer : moveArray) {
			actions.moveByOffset(integer, moveY).perform();
			Thread.sleep(50);
		}
		// actions.moveByOffset((xDis + moveX) / 2, moveY).perform();
		// Thread.sleep((int) (Math.random() * 2000));
		// actions.moveByOffset((xDis + moveX) / 2, moveY).perform();// double
		// Thread.sleep(500);
		actions.release(element).perform();
	}

	@SuppressWarnings("unused")
	private static void printLocation(WebElement element) {
		Point point = element.getLocation();
		System.out.println("final:" + point.toString());// (632,360)
	}

	/**
	 * 等待元素加载，10s超时
	 *
	 * @param driver
	 * @param by
	 */
	public static void waitForLoad(final WebDriver driver, final By by) {
		new WebDriverWait(driver, 10).until(new

		ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver d) {
				WebElement element = driver.findElement(by);
				if (element != null) {
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * 计算需要平移的距离
	 *
	 * @param driver
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage getGapImage(WebDriver driver)
			throws IOException {
		String pageSource = driver.getPageSource(); // pageSource即网页源代码
		String base64GapImage = getFullImageUrl(pageSource);
		Base64Util.getPicFormatBASE64(base64GapImage, basePath + GAP_IMAGE_NAME
				+ ".png");
		// ImageUtil.binaryImage(basePath + GAP_IMAGE_NAME + ".png");
		BufferedImage gapImage = ImageIO.read(new File(basePath
				+ GAP_IMAGE_NAME + ".png"));
		return gapImage;
	}

	public static List<BufferedImage> getLocalALLImage(String path)
			throws IOException {
		List<String> fileNameList = FileUtil.getAllFileName(path);
		List<BufferedImage> imageList = new ArrayList<BufferedImage>();
		for (String fileName : fileNameList) {
			BufferedImage bufferedImage = ImageIO.read(new File(fileName));
			imageList.add(bufferedImage);
		}
		return imageList;
	}

	/**
	 * 获取原始图url
	 *
	 * @param pageSource
	 * @return
	 */
	private static String getFullImageUrl(String pageSource) {
		String base64GapImage = null;
		Document document = Jsoup.parse(pageSource);
		String style = document
				.select("#JDJRV-wrap-loginsubmit > div > div > div > div.JDJRV-img-panel.JDJRV-click-bind-suspend > div.JDJRV-img-wrap > div.JDJRV-bigimg > img")
				.attr("src");
		base64GapImage = style.split(",")[1];
		return base64GapImage;
	}

	public static String[][] getPX(BufferedImage bi) {
		int[] rgb = new int[3];
		int width = bi.getWidth();
		int height = bi.getHeight();
		int minx = bi.getMinX();
		int miny = bi.getMinY();
		String[][] list = new String[width][height];
		for (int i = minx; i < width; i++) {
			for (int j = miny; j < height; j++) {
				int pixel = bi.getRGB(i, j);
				rgb[0] = (pixel & 0xff0000) >> 16;
				rgb[1] = (pixel & 0xff00) >> 8;
				rgb[2] = (pixel & 0xff);
				list[i][j] = rgb[0] + "," + rgb[1] + "," + rgb[2];
			}
		}
		return list;
	}

	public static int compareImage(BufferedImage img1, BufferedImage img2) {
		// 分析图片相似度 begin
		String[][] list1 = getPX(img1);
		String[][] list2 = getPX(img2);
		int xiangsi = 0;
		int busi = 0;
		int i = 0, j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m = 0; m < strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n = 0; n < value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k])
								- Integer.parseInt(value2[k])) < 5) {
							xiangsi++;
						} else {
							busi++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}
		list1 = getPX(img1);
		list2 = getPX(img2);
		i = 0;
		j = 0;
		for (String[] strings : list1) {
			if ((i + 1) == list1.length) {
				continue;
			}
			for (int m = 0; m < strings.length; m++) {
				try {
					String[] value1 = list1[i][j].toString().split(",");
					String[] value2 = list2[i][j].toString().split(",");
					int k = 0;
					for (int n = 0; n < value2.length; n++) {
						if (Math.abs(Integer.parseInt(value1[k])
								- Integer.parseInt(value2[k])) < 5) {
							xiangsi++;
						} else {
							busi++;
						}
					}
				} catch (RuntimeException e) {
					continue;
				}
				j++;
			}
			i++;
		}
		String baifen = "";
		try {
			baifen = ((Double.parseDouble(xiangsi + "") / Double
					.parseDouble((busi + xiangsi) + "")) + "");
			baifen = baifen.substring(baifen.indexOf(".") + 1,
					baifen.indexOf(".") + 3);
		} catch (Exception e) {
			baifen = "0";
		}
		if (baifen.length() <= 0) {
			baifen = "0";
		}
		if (busi == 0) {
			baifen = "100";
		}
		// System.out.println("相似像素数量：" + xiangsi + " 不相似像素数量：" + busi + " 相似率："
		// + Integer.parseInt(baifen) + "%");
		return Integer.parseInt(baifen);
	}

	public static int getSliceOffset(BufferedImage origin, BufferedImage slice) {
		int offset = 0;
		int xMax = origin.getWidth();
		int yMax = origin.getHeight();
		int xyMin = 2;
		// 遍历x
		out: for (int x = xyMin; x < xMax; x++) {
			// 遍历y
			for (int y = xyMin; y < yMax; y++) {
				// 若量图片RGB差值不在阈值范围内，则为缺口位置,阈值需要自己定义
				if (Math.abs(origin.getRGB(x, y) - slice.getRGB(x, y)) > 4000000) {
					offset = x;
					break out;
				}
			}
		}
		return offset;
	}

	public static List<Integer> GetSpeedArray(int distance) {
		List<Integer> arr = new ArrayList<Integer>();

		double current = 0;
		int mid = distance * 1 / 2;
		// 计算间隔
		double t = 0.3;// 反复试验
		// 初速度
		double v = 0;
		double v0 = 0;
		while (current < distance) {
			int a = 0;
			if (current < mid) {
				a = 2;
			} else {
				a = -2;
			}
			v0 = v;
			// 当前速度
			v = v0 + a * t;

			// 移动距离
			double move = v0 * t + 1 / 2 * a * t * t;
			// 当前位移
			int moveint = (int) Math.round(move);
			current += moveint;
			arr.add(moveint);
		}
		return arr;
	}
}
