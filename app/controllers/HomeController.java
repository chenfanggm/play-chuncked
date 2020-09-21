package controllers;

import akka.actor.Status;
import play.mvc.*;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.NotUsed;
import akka.util.ByteString;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {


    public Result index() {
      final ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(20);

      // Prepare a chunked text stream
        Source<ByteString, ?> source =
          Source.<ByteString>actorRef(10, OverflowStrategy.fail())
            .mapMaterializedValue(
              sourceActor -> {
                  for (int i = 0; i < 20; i++) {
                    int foo = i;
                    if (foo < 14) {
                      scheduledThreadPoolExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                          sourceActor.tell(ByteString.fromString("kiki" + foo), null);
                        }
                      }, 1, TimeUnit.MILLISECONDS);
                    } else {
                      scheduledThreadPoolExecutor.schedule(new Runnable() {
                        @Override
                        public void run() {
                          sourceActor.tell(ByteString.fromString("kiki" + foo), null);
                        }
                      }, 10, TimeUnit.MILLISECONDS);
                    }
                  }

                  return NotUsed.getInstance();
              });

        // Serves this stream with 200 OK
        return ok().chunked(source);
    }
}
