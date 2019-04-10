package io.rainfall.store;

import io.rainfall.store.data.Payload;
import io.rainfall.store.values.OutputLog;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

import static io.rainfall.store.data.CompressionFormat.LZ4;
import static io.rainfall.store.data.CompressionServiceFactory.compressionService;
import static io.rainfall.store.data.Payload.raw;

public class Utils {

  public static Payload readBytes(String fileName) throws IOException {
    try (InputStream is = Utils.class.getResourceAsStream(fileName)) {
      byte[] bytes = IOUtils.toByteArray(is);
      return raw(bytes);
    }
  }

  public static OutputLog toLz4CompressedGetOutput(String hlog) {
    String operation = "GET";
    return toLz4CompressedOutput(hlog, operation);
  }

  public static OutputLog toLz4CompressedOutput(String hlog, String operation) {
    try {
      Payload raw = readBytes(hlog);
      Payload compressed = compressionService(LZ4)
              .compress(raw.getData());
      return OutputLog.builder()
              .operation(operation)
              .format("hlog")
              .payload(compressed)
              .build();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
