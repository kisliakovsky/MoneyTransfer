package ru.devsand.transmitter.rest;

import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import org.hamcrest.number.BigDecimalCloseTo;
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
import static org.hamcrest.number.BigDecimalCloseTo.closeTo;


public class BasicRestServiceTest {

    private static final BigDecimal ACCURACY = BigDecimal.valueOf(0.01);
    private static final String PATH_SEPARATOR = "/";
    private static final int PORT = 8080;
    private URL apiAddress;
    private StringJoiner pathJoiner;
    private RestService restService;

    @Before
    public void setUpTest() throws Exception {
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
        URL hostAddress = new URL("http", "localhost", PORT, "");
        apiAddress = new URL(hostAddress, "api/transfers");
        pathJoiner = new StringJoiner(PATH_SEPARATOR);
        restService = new BasicRestService(PORT);
        restService.start();
    }

    @After
    public void tearDownTest() throws Exception {
        restService.stop();
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
        assertThat(transfer.getSenderAccount().getBalance(), closeTo(BigDecimal.valueOf(800), ACCURACY));
        assertThat(transfer.getReceiverAccount().getBalance(), closeTo(BigDecimal.valueOf(2200), ACCURACY));
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(1));
    }

    @Test
    public void checkMakePhoneTransfer() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("makebyphone");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PhoneRequest("+79111111111", "+79055555555", BigDecimal.valueOf(200)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("SUCCESS"));
        Transfer transfer = transferResponse.getData();
        assertThat(transfer.getSenderAccount().getBalance(), closeTo(BigDecimal.valueOf(800), ACCURACY));
        assertThat(transfer.getReceiverAccount().getBalance(), closeTo(BigDecimal.valueOf(3200), ACCURACY));
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(1));
    }

    @Test
    public void checkTransferWithExceedingAmountOfMoney() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("makebyphone");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PhoneRequest("+79111111111", "+79055555555", BigDecimal.valueOf(20000)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("ERROR"));
        assertThat(transferResponse.getMessage(), equalTo("Insufficient funds"));
    }

    @Test
    public void checkTransferByInvalidPhoneNumber() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("makebyphone");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PhoneRequest("+7911111111", "+79055555555", BigDecimal.valueOf(20000)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("ERROR"));
        assertThat(transferResponse.getMessage(), equalTo("Customer not found"));
    }

    @Test
    public void checkTransferByInvalidAccountId() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("make");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PlainRequest(11, 3, BigDecimal.valueOf(200)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("ERROR"));
        assertThat(transferResponse.getMessage(), equalTo("Account not found"));
    }

    @Test
    public void checkInvalidRequest() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("make");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body("{}")
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("ERROR"));
        assertThat(transferResponse.getMessage(), equalTo("Invalid request"));
    }

    @Test
    public void checkMultiCurrencyRequest() throws Exception {
        pathJoiner.add("transfers");
        pathJoiner.add("make");
        String firstRequestAddress = new URL(apiAddress, pathJoiner.toString()).toString();
        HttpResponse<TransferResponse> firstResponse = Unirest.post(firstRequestAddress)
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .body(new PlainRequest(6, 1, BigDecimal.valueOf(200)))
                .asObject(TransferResponse.class);
        TransferResponse transferResponse = firstResponse.getBody();
        assertThat(transferResponse.getStatus(), equalTo("SUCCESS"));
        Transfer transfer = transferResponse.getData();
        assertThat(transfer.getSenderAccount().getBalance(), closeTo(BigDecimal.valueOf(2800), ACCURACY));
        assertThat(transfer.getReceiverAccount().getBalance(), closeTo(BigDecimal.valueOf(13385.82), ACCURACY));
        HttpResponse<TransfersResponse> response = Unirest.get(apiAddress.toString())
                .asObject(TransfersResponse.class);
        TransfersResponse transfersResponse = response.getBody();
        assertThat(transfersResponse.getStatus(), equalTo("SUCCESS"));
        assertThat(transfersResponse.getData().size(), is(1));
    }

}