package pers.liaohaolong.mokulibserver.service.base.impl;

import org.jspecify.annotations.Nullable;
import org.springframework.stereotype.Service;
import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.config.properties.ImageProperties;
import pers.liaohaolong.mokulibserver.exception.BusinessException;
import pers.liaohaolong.mokulibserver.service.base.ImageService;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service
public class ImageServiceImpl implements ImageService {

    @Override
    public void save(ImageConfigurations.ImageType imageType, String fileName, byte[] stream) throws BusinessException {
        // 获取图片配置
        ImageProperties properties = imageType.getImageProperties();
        // 转为流
        try (InputStream inputStream = new ByteArrayInputStream(stream)) {
            // 解析文件类型
            String formatName = analyzeImageType(inputStream);
            // 检查类型是否受支持
            if (ImageProperties.isUnsupportedWriteFormat(formatName))
                throw new BusinessException("不支持的文件类型：" + formatName);
            // 重置输入流
            inputStream.reset();
            // 创建目标文件
            File dest = new File(properties.getAbsolutePath(fileName, properties.getSaveFormat()));
            // 保存图片
            ImageIO.write(ImageIO.read(inputStream), properties.getSaveFormat(), dest);
        } catch (IOException e) {
            throw new BusinessException("文件保存失败：" + e.getMessage());
        }
    }

    /**
     * 根据 {@link ImageReader#getFormatName} 方法，解析文件的真实类型。
     *
     * @param inputStream 图片输入流
     * @return 文件类型，解析失败时返回 null
     */
    private @Nullable String analyzeImageType(InputStream inputStream) {
        try (ImageInputStream iis = ImageIO.createImageInputStream(inputStream)) {
            Iterator<ImageReader> iter = ImageIO.getImageReaders(iis);
            if (iter.hasNext()) {
                ImageReader reader = iter.next();
                return reader.getFormatName();
            }
        } catch (IOException ignore) {
        }
        return null;
    }

}
