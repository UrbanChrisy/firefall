package nz.co.delacour.firefall.core.util;

import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.common.collect.Lists;
import lombok.var;
import nz.co.delacour.firefall.core.FirefullService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;

import java.io.Closeable;
import java.io.IOException;

import static nz.co.delacour.firefall.core.FirefullService.fir;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 * Created by Chris on 13/10/19.
 * ▬▬ι═══════ﺤ            -═══════ι▬▬
 */

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TestBase {

    private Closeable session;

    @BeforeAll
    public void setup() {
        initMocks(this);
        var options = FirestoreOptions.newBuilder().setProjectId("firefull-tests").build();
        FirefullService.init(options.getService());
        this.session = FirefullService.begin();
    }


    @AfterEach
    public void afterEach() {
        var collections = Lists.newArrayList(fir().factory().getFirestore().listCollections());
        for (CollectionReference collection : collections) {
            var documents = collection.listDocuments();
            for (DocumentReference document : documents) {
                try {
                    document.delete().get();
                } catch (Exception ignore) {}
            }
        }
    }

    @AfterAll
    public void tearDown() {
        try {
            this.session.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
