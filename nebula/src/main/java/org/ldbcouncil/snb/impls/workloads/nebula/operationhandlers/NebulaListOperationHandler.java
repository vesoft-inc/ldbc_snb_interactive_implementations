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
        long startTime = System.currentTimeMillis();
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
        } finally {
            long threadID = Thread.currentThread().getId();
            long consumeTime = (System.currentTimeMillis() - startTime);
            String name = operation.getClass().getSimpleName();
            if (name.startsWith("LdbcQuery")) {
                // only output IC query
                System.out.println("Query SimpleName : " + name + " threadID : " + threadID + " startTime : " + startTime / 1000 + " consumeTime : " + consumeTime + "ms");
            }
        }
    }

    public abstract TOperationResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException;

}
