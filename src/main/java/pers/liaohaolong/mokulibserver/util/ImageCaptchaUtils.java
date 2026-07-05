package pers.liaohaolong.mokulibserver.util;

import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

public final class ImageCaptchaUtils {

    /**
     * 验证码字符集
     */
    public static final String CAPTCHA_CHARACTER_SET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    /**
     * 验证码字体
     */
    public static final Font CAPTCHA_FONT = new Font("宋体", Font.BOLD, 28);

    /**
     * 验证码图片宽度
     */
    public static final  int CAPTCHA_IMAGE_WIDTH = 120;

    /**
     * 验证码图片高度
     */
    public static final  int CAPTCHA_IMAGE_HEIGHT = 30;

    private static final Random RANDOM = new Random();

    /**
     * 字符随机旋转角度
     */
    private static final int CAPTCHA_ROTATE = 30;

    private ImageCaptchaUtils() {
    }

    /**
     * 生成验证码
     *
     * @return 验证码
     */
    public static @NotNull String generateCaptchaText() {
        StringBuilder verificationCode = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            int index = RANDOM.nextInt(CAPTCHA_CHARACTER_SET.length());
            verificationCode.append(CAPTCHA_CHARACTER_SET.charAt(index));
        }
        return verificationCode.toString();
    }

    /**
     * 生成验证码图片
     *
     * @param verificationCode 验证码
     * @return 验证码图片
     */
    public static byte[] generateCaptchaImage(@NotNull String verificationCode) throws IOException {
        // 创建对象,验证码图片对象
        BufferedImage image = new BufferedImage(CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT, BufferedImage.TYPE_INT_RGB);

        // 填充背景色
        Graphics2D g = (Graphics2D) image.getGraphics(); // 画笔对象,2D来旋转验证码字母
        g.setColor(Color.WHITE); // 设置画笔颜色
        g.fillRect(0, 0, CAPTCHA_IMAGE_WIDTH, CAPTCHA_IMAGE_HEIGHT);

        // 改变字体
        g.setFont(CAPTCHA_FONT);
        // 将验证码偏转并写到画布上
        for (int i = 1; i <= 4; i++) {
            int x = CAPTCHA_IMAGE_WIDTH / 5 * i;
            int y = CAPTCHA_IMAGE_HEIGHT / 3 * 2;
            // 获取正负30的角度
            int angle = RANDOM.nextInt(CAPTCHA_ROTATE * 2) - CAPTCHA_ROTATE;
            double radian = angle * Math.PI / 180;
            // 用随机产生的颜色将验证码绘制到图像中
            g.setColor(new Color(RANDOM.nextInt(255), RANDOM.nextInt(255), RANDOM.nextInt(255)));
            // 设置旋转角度
            g.rotate(radian, x, y);
            // 把字符画在画布上
            g.drawString(String.valueOf(verificationCode.charAt(i - 1)), x, y);
            // 重置旋转
            g.rotate(-radian, x, y);
        }

        // 随机产生干扰线
        g.setColor(Color.MAGENTA);
        for (int i = 0; i < 10; i++) {
            // 随机生成坐标点
            int x1 = RANDOM.nextInt(CAPTCHA_IMAGE_WIDTH * 2) - CAPTCHA_IMAGE_WIDTH;
            int y1 = RANDOM.nextInt(CAPTCHA_IMAGE_HEIGHT * 2) - CAPTCHA_IMAGE_HEIGHT;
            int x2 = RANDOM.nextInt(CAPTCHA_IMAGE_WIDTH * 2) - CAPTCHA_IMAGE_WIDTH;
            int y2 = RANDOM.nextInt(CAPTCHA_IMAGE_HEIGHT * 2) - CAPTCHA_IMAGE_HEIGHT;
            g.drawLine(x1, y1, x2, y2);
        }

        // 将 BufferedImage 转换为字节数组
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

}
