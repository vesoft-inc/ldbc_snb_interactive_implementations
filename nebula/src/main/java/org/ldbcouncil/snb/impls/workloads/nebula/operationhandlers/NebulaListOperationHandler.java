package org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers;

import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.Operation;
import org.ldbcouncil.snb.driver.ResultReporter;
import org.ldbcouncil.snb.impls.workloads.nebula.NebulaDbConnectionState;
import org.ldbcouncil.snb.impls.workloads.operationhandlers.ListOperationHandler;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.exception.IOErrorException;
import com.vesoft.nebula.client.graph.net.Session;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public abstract class NebulaListOperationHandler <TOperation extends Operation<List<TOperationResult>>, TOperationResult>
        implements ListOperationHandler<TOperationResult, TOperation, NebulaDbConnectionState> {
    @Override
    public void executeOperation(TOperation operation, NebulaDbConnectionState state, ResultReporter resultReporter) throws DbException {
        Session session = state.getSession();
        List<TOperationResult> results = new ArrayList<>();
        int resultCount = 0;
        results.clear();

        final String queryString = getQueryString(state, operation);
        state.logQuery(operation.getClass().getSimpleName(), queryString);
        final ResultSet result;
        try {
            result = session.execute(queryString);
            if (state.isPrintErrors() && !result.isSucceeded()) {
                System.out.println(result.getErrorMessage());
            }
            long rowSize = result.rowsSize();
            while (resultCount < rowSize) {
                final ResultSet.Record record = result.rowValues(resultCount);

                resultCount++;
                TOperationResult tuple = convertSingleResult(record);
                if (state.isPrintResults()) {
                    System.out.println(tuple.toString());
                }
                results.add(tuple);
            }
            resultReporter.report(resultCount, results, operation);

        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    public abstract TOperationResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException;

}
