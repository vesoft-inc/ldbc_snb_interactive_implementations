package org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers;

import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.Operation;
import org.ldbcouncil.snb.driver.ResultReporter;
import org.ldbcouncil.snb.impls.workloads.nebula.NebulaDbConnectionState;
import org.ldbcouncil.snb.impls.workloads.operationhandlers.SingletonOperationHandler;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.net.Session;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;

public abstract class NebulaSingletonOperationHandler <TOperation extends Operation<TOperationResult>, TOperationResult>
        implements SingletonOperationHandler<TOperationResult, TOperation, NebulaDbConnectionState> {
    @Override
    public void executeOperation(TOperation operation, NebulaDbConnectionState state, ResultReporter resultReporter) throws DbException {
        Session session = state.getSession();
        try {
            TOperationResult tuple = null;
            int resultCount = 0;

            final String queryString = getQueryString(state, operation);
            state.logQuery(operation.getClass().getSimpleName(), queryString);
            final ResultSet result = session.execute(queryString);
            if (state.isPrintErrors() && !result.isSucceeded()) {
                System.out.println(result.getErrorMessage());
            }
            if (!result.isSucceeded()) {
                resultReporter.report(0, noResult(), operation);
                return;
            }
            if (result.rowsSize() > 0) {
                final ResultSet.Record record = result.rowValues(0);
                resultCount++;

                tuple = convertSingleResult(record);

                if (state.isPrintResults()) {
                    System.out.println(tuple.toString());
                }
            }

            resultReporter.report(resultCount, tuple, operation);
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public abstract TOperationResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException;

    public abstract TOperationResult  noResult();
}
