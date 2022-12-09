package org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers;

import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.Operation;
import org.ldbcouncil.snb.driver.ResultReporter;
import org.ldbcouncil.snb.driver.workloads.interactive.*;
import org.ldbcouncil.snb.impls.workloads.nebula.NebulaDbConnectionState;
import org.ldbcouncil.snb.impls.workloads.operationhandlers.UpdateOperationHandler;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;

public abstract class NebulaUpdateOperationHandler <TOperation extends Operation<LdbcNoResult>>
        implements UpdateOperationHandler<TOperation, NebulaDbConnectionState> {
    @Override
    public void executeOperation(TOperation operation, NebulaDbConnectionState state, ResultReporter resultReporter) throws DbException {
        Session session = state.getSession();
            try {
                final String queryString = getQueryString(state, operation);
                state.logQuery(operation.getClass().getSimpleName(), queryString);
                ResultSet result = session.execute(queryString);
                if (state.isPrintErrors() && !result.isSucceeded()) {
                    System.out.println(result.getErrorMessage());
                }
            } catch (Exception e) {
                throw new DbException(e);
            }
            resultReporter.report(0, LdbcNoResult.INSTANCE, operation);
    }
}
