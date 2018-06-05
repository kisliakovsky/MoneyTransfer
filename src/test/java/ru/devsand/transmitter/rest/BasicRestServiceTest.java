package ru.devsand.transmitter.rest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import ru.devsand.transmitter.model.entity.Transfer;

import java.math.BigDecimal;
import java.net.URL;
import java.util.StringJoiner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;


public class BasicRestServiceTest {

    private static final String PATH_SEPARATOR = "/";
    private static final int PORT = 8080;
    private URL hostAddress;
    private URL apiAddress;
    private StringJoiner pathJoiner;
    private RestService restService;

    @BeforeClass
    public static void setUp() {
        Unirest.setObjectMapper(new ObjectMapper() {
            Gson gson = new Gson();
            @Override
            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return gson.fromJson(value, valueType);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            @Override
            public String writeValue(Object value) {
                try {
                    return gson.toJson(value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Before
    public void setUpTest() throws Exception {
        hostAddress = new URL("http", "localhost", PORT, "");
        apiAddress = new URL(hostAddress, "api/transfers");
        pathJoiner = new StringJoiner(PATH_SEPARATOR);
        restService = new BasicRestService(PORT);
        restService.start();
    }

    @After
    public void tearDownTest() throws Exception {
        restService.stop();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        Unirest.shutdown();
    }

    @Test
    public void checkGetTransfersByDefault() throws Exception {
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(0));
    }

    @Test
    public void checkMakePlainTransfer() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("make");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PlainRequest(1, 3, BigDecimal.valueOf(200)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("SUCCESS"));
        Transfer transfer = transferResponse.getData();
        assertThat(transfer.getSenderAccount().getBalance(), equalTo(BigDecimal.valueOf(800)));
        assertThat(transfer.getReceiverAccount().getBalance(), equalTo(BigDecimal.valueOf(2200)));
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(1));
    }

    @Test
    public void checkMakePhoneTransfer() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("make");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PhoneRequest("+79111111111", "+79055555555", BigDecimal.valueOf(200)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("SUCCESS"));
        Transfer transfer = transferResponse.getData();
        assertThat(transfer.getSenderAccount().getBalance(), equalTo(BigDecimal.valueOf(800)));
        assertThat(transfer.getReceiverAccount().getBalance(), equalTo(BigDecimal.valueOf(2200)));
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(1));
    }

}