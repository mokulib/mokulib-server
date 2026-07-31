package pers.liaohaolong.mokulibserver.service.base;

import pers.liaohaolong.mokulibserver.config.ImageConfigurations;
import pers.liaohaolong.mokulibserver.exception.BusinessException;

public interface ImageService {

    void save(ImageConfigurations.ImageType imageType, String fileName, byte[] stream) throws BusinessException;

}
