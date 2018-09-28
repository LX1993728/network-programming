package com.liuxun.netty.serial;

import io.netty.handler.codec.marshalling.*;
import org.jboss.marshalling.MarshallerFactory;
import org.jboss.marshalling.Marshalling;
import org.jboss.marshalling.MarshallingConfiguration;

/**
 * @author liuxun
 * @apiNote Marshalling工厂
 */
public final class MarshallingCodeCFactory {

    /**
     * @return MarshallingDecoder
     * @apiNote 创建JBoss Marshalling解码器MarshallingDecoder
     */
    public static MarshallingDecoder buildMarshallingDecoder() {
        // 首先通过Marshalling工具类的经典方法获取Marshalling实例对象 参数serial创建的是java序列化工厂对象
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        // 创建MarshallingConfiguration对象，配置了版本号为5
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        // 根据marshallingFactory和configuration创建provider
        UnmarshallerProvider provider = new DefaultUnmarshallerProvider(marshallerFactory, configuration);
        // 构建Netty的MarshallingDecoder对象，两个参数分别为provider何单个消息序列化后的最大长度
        MarshallingDecoder decoder = new MarshallingDecoder(provider, 1024 * 1024);
        return decoder;
    }

    /**
     * @return MarshallingEncoder
     * @apiNote 创建JBoss Marshalling编码器MarshallingEncoder
     */
    public static MarshallingEncoder buildMarshallingEncoder() {
        final MarshallerFactory marshallerFactory = Marshalling.getProvidedMarshallerFactory("serial");
        final MarshallingConfiguration configuration = new MarshallingConfiguration();
        configuration.setVersion(5);
        MarshallerProvider provider = new DefaultMarshallerProvider(marshallerFactory, configuration);
        // 构建Netty的MarshallingEncoder对象，MarshallingEncoder用于将实现序列化接口的POJO对象序列化为二进制数组
        MarshallingEncoder encoder = new MarshallingEncoder(provider);
        return encoder;
    }
}
