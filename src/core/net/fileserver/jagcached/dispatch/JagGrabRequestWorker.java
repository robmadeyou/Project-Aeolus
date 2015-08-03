package core.net.fileserver.jagcached.dispatch;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;

import core.net.fileserver.jagcached.fs.IndexedFileSystem;
import core.net.fileserver.jagcached.net.jaggrab.JagGrabRequest;
import core.net.fileserver.jagcached.net.jaggrab.JagGrabResponse;
import core.net.fileserver.jagcached.resource.ResourceProvider;
import core.net.fileserver.jagcached.resource.VirtualResourceProvider;

/**
 * A worker which services JAGGRAB requests.
 * @author Graham
 */
public final class JagGrabRequestWorker extends RequestWorker<JagGrabRequest, ResourceProvider> {

	/**
	 * Creates the JAGGRAB request worker.
	 * @param fs The file system.
	 */
	public JagGrabRequestWorker(IndexedFileSystem fs) {
		super(new VirtualResourceProvider(fs));
	}

	@Override
	protected ChannelRequest<JagGrabRequest> nextRequest() throws InterruptedException {
		return RequestDispatcher.nextJagGrabRequest();
	}

	@Override
	protected void service(ResourceProvider provider, Channel channel, JagGrabRequest request) throws IOException {
		ByteBuffer buf = provider.get(request.getFilePath());
		if (buf == null) {
			channel.close();
		} else {
			ChannelBuffer wrapped = ChannelBuffers.wrappedBuffer(buf);
			channel.write(new JagGrabResponse(wrapped)).addListener(ChannelFutureListener.CLOSE);
		}
	}

}
