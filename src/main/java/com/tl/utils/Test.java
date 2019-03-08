package com.tl.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

public class Test {
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

	private static String basePath = "plugins/images/";
	private static String GAP_IMAGE_NAME = "full-image";

	public static int getMoveDistance(BufferedImage gapImage, String path)
			throws IOException {
		List<BufferedImage> localImageList = getLocalALLImage(path);
		int move = 0;
		boolean stop = false;
		for (BufferedImage bufferedImage : localImageList) {
			boolean falg = false;
			for (int i = 0; i < gapImage.getWidth(); i++) {
				for (int j = 0; j < gapImage.getHeight(); j++) {
					int[] fullRgb = new int[3];
					fullRgb[0] = (gapImage.getRGB(i, j) & 0xff0000) >> 16;
					fullRgb[1] = (gapImage.getRGB(i, j) & 0xff00) >> 8;
					fullRgb[2] = (gapImage.getRGB(i, j) & 0xff);

					int[] bgRgb = new int[3];
					bgRgb[0] = (bufferedImage.getRGB(i, j) & 0xff0000) >> 16;
					bgRgb[1] = (bufferedImage.getRGB(i, j) & 0xff00) >> 8;
					bgRgb[2] = (bufferedImage.getRGB(i, j) & 0xff);
					if (difference(fullRgb, bgRgb) > 255) {
						if (j == 0) {
							falg = true;
							break;
						}
						if (i > 10) {
							move = i;
							falg = true;
							break;
						}
					}
				}
				if (falg) {
					break;
				}
			}
			if (stop) {
				break;
			}
		}
		return move;
	}

	public static BufferedImage getGapImage(WebDriver driver)
			throws IOException {
		String pageSource = driver.getPageSource(); // pageSource即网页源代码
		String base64GapImage = getFullImageUrl(pageSource);
		Base64Util.getPicFormatBASE64(base64GapImage, basePath + GAP_IMAGE_NAME
				+ ".png");
		BufferedImage gapImage = ImageIO.read(new File(basePath
				+ GAP_IMAGE_NAME + ".png"));
		return gapImage;
	}

	private static int difference(int[] a, int[] b) {
		return Math.abs(a[0] - b[0]) + Math.abs(a[1] - b[1])
				+ Math.abs(a[2] - b[2]);
	}

	public static void grayImage(String filePath) throws IOException {
		BufferedImage image = ImageIO.read(new File(filePath));
		int width = image.getWidth();
		int height = image.getHeight();

		BufferedImage grayImage = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);// 重点，技巧在这个参数BufferedImage.TYPE_BYTE_GRAY
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				int rgb = image.getRGB(i, j);
				grayImage.setRGB(i, j, rgb);
			}
		}
		String[] fileName = filePath.split("//");
		String s = fileName[(fileName.length - 1)];
		File newFile = new File(s);
		ImageIO.write(grayImage, "png", newFile);
	}

	private static String getFullImageUrl(String pageSource) {
		String base64GapImage = null;
		Document document = Jsoup.parse(pageSource);
		String style = document
				.select("#JDJRV-wrap-loginsubmit > div > div > div > div.JDJRV-img-panel.JDJRV-click-bind-suspend > div.JDJRV-img-wrap > div.JDJRV-bigimg > img")
				.attr("src");
		base64GapImage = style.split(",")[1];
		return base64GapImage;
	}

	public static void main(String[] args) throws IOException {
		String path = "plugins/images/";
		BufferedImage image = ImageIO.read(new File(path + "full-image.png"));
		BufferedImage image1 = ImageIO.read(new File(path + "6.png"));
		int xMax = image.getWidth();
		int yMax = image.getHeight();
		int xyMin = 2;
		int offset = 0;
		System.out.println(xMax + "---"+yMax);
		out: for (int x = xyMin; x < xMax; x++) {
			// 遍历y
			for (int y = xyMin; y < yMax; y++) {
				// 若量图片RGB差值不在阈值范围内，则为缺口位置,阈值需要自己定义
				if (Math.abs(image.getRGB(x, y) - image1.getRGB(x, y)) > 4000000) {
					offset = x;
					break out;
				}
			}
		}
		System.out.println(offset);
	}
}
