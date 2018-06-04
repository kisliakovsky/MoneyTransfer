package ru.devsand.transmitter.model.repository;

import com.j256.ormlite.support.ConnectionSource;
import ru.devsand.transmitter.model.entity.Transfer;

import java.sql.SQLException;

public class BasicTransferRepository extends AbstractRepository<Transfer, Long>
        implements TransferRepository {

    private ConnectionSource connectionSource;

    public BasicTransferRepository(ConnectionSource connectionSource)
            throws SQLException {
        super(connectionSource, Transfer.class);
    }

}
