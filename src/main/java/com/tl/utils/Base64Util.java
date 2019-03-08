package com.tl.utils;

import java.io.FileInputStream;
import java.io.RandomAccessFile;

public class Base64Util {
	/**
	 * 图片BASE64 编码
	 * 
	 * @author zhujie
	 */
	@SuppressWarnings("restriction")
	public static String getPicBASE64(String picPath) {
		String content = null;
		try {
			FileInputStream fileForInput = new FileInputStream(picPath);
			byte[] bytes = new byte[fileForInput.available()];
			fileForInput.read(bytes);
			content = new sun.misc.BASE64Encoder().encode(bytes); // 具体的编码方法
			fileForInput.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

	/**
	 * 对图片BASE64 解码
	 * 
	 * @author zhujie
	 */
	@SuppressWarnings("restriction")
	public static void getPicFormatBASE64(String str, String picPath) {
		try {
			byte[] result = new sun.misc.BASE64Decoder().decodeBuffer(str
					.trim());
			RandomAccessFile inOut = new RandomAccessFile(picPath, "rw"); // r,rw,rws,rwd
			inOut.write(result);
			inOut.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String str = "iVBORw0KGgoAAAANSUhEUgAAADIAAAAyCAYAAAAeP4ixAAAFA0lEQVR42s2ZXU9URxjH6UW9qRe96gdQL/oNNFUUWcBEG9aaVQjCylpZE6iVprzsLrAgK6JgVCCSFlDbpr61hBLT3rRNatImNTFNb4yYSEyvemErTWNQWNBxnmGfc+bMzpwzZ89Zd0n+mTmzM3v+v3nmdSkp8fAXi8XKZ4YiZDjZSrriHUyQh7JEIhEoKfa/zs7OAxOpZkJBnPQL1C1WiDNodPJkC7kzWkseX9lD0lcrSfrLSpb/fayWfmaCQpuigojH44No7tcLdeTFVwGLXn5tff5tpM6AgbbFEokwmvpzbH8WhEpQl4tMuNAcb9hFwknQBtvDdxWMghqoABNTdIK7hUBNmYtDRSGH1QSYuDNSmzMItM0Mr4lCTvJ5MPF4cjdZ/aJcKTuQfy7twUk/X8ihtQwmlq9U2ILYCdpmhtZyXkzODkZ0NjamxcuVOUG8uBxge4zuezKRG3fb21pfPHM6YpjSNS/qu8HDrmByApG9+HWIh8a8J5DVz8pNTQaylFeYSWvqCWRltCxbF3caEgFFE37CeAJJnytTCsGMPAeGL/cTLm8gToCq4Zir8gayNLjdkqaHdpifZ/JGpIoBBIyK4suZaSoxz3/uB5AnEN6MCMCX64hFSLJAvFYQP2VExmZZzxKt5wmkK9ZBhhKtZDoZIXOpal9AIDIWCH6vshF3THnCpbepPunr61uvOtmOy44Hn3c3kz/692WbS63JCeJ5fylLdc3zmhmwP//Rq0BSCkMp14FopU1UUVrxZ2z0fU+DYV5HYH6xZxsDYZMf54pLzV8IkqfjVSwPKTzfOtVoOVS2tbW9o3MH+Rgb3UwcMqLxV3e5JTLiMwpAcI9xC4P1xRT0aCRIrqWa3J2QaeUahLnbG5KaBUEEQAAF5ZDHaORD/41Vkev9RxAmpXs7NCKzmDRNo2FebDilzOU3n3p0vtoYZnRavK0FM5E4yhrc634/yzwCQMrvIVLhQXTUAVSsp0hnU8ac+Ug3Kk3Q4JtEoxTEsquLp2fetN0J266NIn0wvBeH149aIO3t7RuhwZnYcUsUjAiM+iQVrAyGaoHOlUxE/tYCySzNbNPUAtAxpDBnWy5o6WLA3Q8aPIjqS6X3Fo9y+p6F8y4jQituYEOrq9XxpboQKnBZe1XZ3ICxcv2kO9kboMGNE0cshsVLlviZKi+rb9fOeKYLyjLdAlY+pXO0uZTMHgu7W7WmetaW3/ung1Izbm+Vqmexx5lg442vGV/9cCtJ120hz0ObycODVe72EXruOmZevnZqGXTqZaUydx7sdTC+VLNm/NkHm1kK+remlFxviejv7JYjyqn9Od3rncQbh15fCb9nGGfmgyYAPD+sryLXWhr1z1r80eSH/rB/5uGKzA0XMM73OupB/S6yENrG8pDO1e0is9GD6tPvyvBWEo1G36SFb9EP34VdHOcEO8bLIPgfH/BZLOPLU9ZeB+O8aVHTRxvc30dmeg8pG9w9GTLNiJIBcMb5CSrrcTvldEPERsl4BznbdZx82xsh9wfo6jRUpgawGSqwqoBxvtfZOOdklIfsQXL7OUjV84Jkk5Pv8cVqq1ldQbu8gojG+bUcXo5S9Tro/+AWV1CeQfDKCsMEjeNQQUO8eexFGYhYT6a8gIjrON/Tomm+XARwIxWcJxBZb4uG+d7X6fFc5QlENtbhWQXoh1RR8gWkGOQJpBjlCmS6qb4oIdz+e/oVtl14hAyJ+/kAAAAASUVORK5CYIJgMw==";
		getPicFormatBASE64(str, "test.png");
	}
}
