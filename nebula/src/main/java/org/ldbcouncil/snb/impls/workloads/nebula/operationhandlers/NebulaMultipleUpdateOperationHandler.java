package org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers;

import org.ldbcouncil.snb.driver.*;
import org.ldbcouncil.snb.driver.workloads.interactive.LdbcNoResult;
import org.ldbcouncil.snb.impls.workloads.nebula.NebulaDbConnectionState;
import org.ldbcouncil.snb.impls.workloads.operationhandlers.MultipleUpdateOperationHandler;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;
import java.util.List;



public abstract class NebulaMultipleUpdateOperationHandler<TOperation extends Operation<LdbcNoResult>>
        implements MultipleUpdateOperationHandler<TOperation, NebulaDbConnectionState> {

    @Override
    public void executeOperation(TOperation operation, NebulaDbConnectionState state, ResultReporter resultReporter) throws DbException {
        Session session = state.getSession();
        long startTime = System.currentTimeMillis();
        try {
            List<String> queryStrings = getQueryString(state, operation);
            for (String queryString : queryStrings) {
                state.logQuery(operation.getClass().getSimpleName(), queryString);
                ResultSet result = session.execute(queryString);
                if (state.isPrintErrors() && !result.isSucceeded()) {
                    System.out.println(result.getErrorMessage());
                }
            }
        } catch (Exception e) {
            throw new DbException(e);
        } finally {
            long threadID = Thread.currentThread().getId();
            long consumeTime = (System.currentTimeMillis() - startTime) / 1000;
            System.out.println("Query SimpleName : " + operation.getClass().getSimpleName());
            System.out.println("threadID : " + threadID + " startTime : " + startTime / 1000 + " consumeTime : " + consumeTime);
        }
        resultReporter.report(0, LdbcNoResult.INSTANCE, operation);
    }
}
