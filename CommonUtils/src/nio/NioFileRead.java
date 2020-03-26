package nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import org.apache.commons.io.FileUtils;

public class NioFileRead {

    public static void main(String[] args) throws IOException {
        String path = NioFileRead.class.getClassLoader().getResource("nio.txt").getPath();
        String path2 = NioFileRead.class.getClassLoader().getResource("nio_out.txt").getPath();
        File file = new File(path);
        File file2 = new File(path2);
        try (FileInputStream inputStream = FileUtils.openInputStream(file);
             FileOutputStream outputStream = FileUtils.openOutputStream(file2);
             FileChannel channel = inputStream.getChannel();
             FileChannel channel2 = outputStream.getChannel();
        ) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            for (; channel.read(buffer) != -1;) {
                buffer.flip();
                channel2.write(buffer);
                buffer.clear();
            }
            buffer = ByteBuffer.wrap("\n新添加中文字符数据！".getBytes(Charset.forName("UTF-8")));
            channel2.write(buffer);
            buffer.clear();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
