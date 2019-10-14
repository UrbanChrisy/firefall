package nz.co.delacour.firefall.core.util;

import com.google.cloud.firestore.FirestoreOptions;
import lombok.var;
import nz.co.delacour.firefall.core.FirefullService;
import org.junit.After;
import org.junit.Before;

import java.io.Closeable;
import java.io.IOException;

import static org.mockito.MockitoAnnotations.initMocks;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

public class TestBase {

    private Closeable session;

    @Before
    public void setup() {
        initMocks(this);

        var options = FirestoreOptions.newBuilder().setProjectId("firefull-tests").build();
        FirefullService.init(options.getService());
        this.session = FirefullService.begin();
    }

    @After
    public void tearDown() {
        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
