package org.ldbcouncil.snb.impls.workloads.nebula;

import org.ldbcouncil.snb.driver.Db;
import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.control.LoggingService;
import org.ldbcouncil.snb.driver.workloads.interactive.*;
import org.ldbcouncil.snb.impls.workloads.db.BaseDb;
import org.ldbcouncil.snb.impls.workloads.nebula.converter.NebulaConverter;
import org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers.NebulaListOperationHandler;
import org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers.NebulaSingletonOperationHandler;
import org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers.NebulaMultipleUpdateOperationHandler;
import org.ldbcouncil.snb.impls.workloads.nebula.operationhandlers.NebulaUpdateOperationHandler;
import com.vesoft.nebula.client.graph.data.ResultSet;
import com.vesoft.nebula.client.graph.data.ValueWrapper;
import com.vesoft.nebula.client.graph.exception.InvalidValueException;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NebulaDb  extends BaseDb<NebulaQueryStore> {

    @Override
    protected void onInit(Map<String, String> properties, LoggingService loggingService) throws DbException {
        try {
            NebulaID.ID_PREFIX_SIZE = Integer.parseInt(properties.get("idPrefixSize"));
            for (String idPrefixKV : properties.get("idPrefix").split(",")) {
                String[] kv = idPrefixKV.split(":");
                String idName = kv[0].trim();
                String idPrefix = kv[1].trim();
                if (idPrefix.length() != NebulaID.ID_PREFIX_SIZE) {
                    throw new DbException("ID prefix of: "+ idPrefix + " not match the prefix size:" + NebulaID.ID_PREFIX_SIZE);
                }
                switch (idName) {
                    case "comment": {
                        NebulaID.COMMENT_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "forum": {
                        NebulaID.FORUM_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "organisation": {
                        NebulaID.ORGANISATION_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "person": {
                        NebulaID.PERSON_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "place": {
                        NebulaID.PLACE_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "post": {
                        NebulaID.POST_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "tag": {
                        NebulaID.TAG_ID_PREFIX = idPrefix;
                        break;
                    }
                    case "tagclass": {
                        NebulaID.TAGCLASS_ID_PREFIX = idPrefix;
                        break;
                    }
                }
            }
            dcs = new NebulaDbConnectionState(properties, new NebulaQueryStore(properties.get("queryDir")));
        } catch (UnknownHostException e) {
            throw new DbException(e.getMessage());
        }
    }

    // Interactive complex reads

    public static class InteractiveQuery1 extends NebulaListOperationHandler<LdbcQuery1, LdbcQuery1Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery1 operation) {
            return state.getQueryStore().getQuery1(operation);
        }

        @Override
        public LdbcQuery1Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {

            List<String> emails = new ArrayList<>();
            if (!record.get(8).isNull()) {
                Collections.addAll(emails, record.get(8).asString().split(","));
            }
            List<String> languages = new ArrayList<>();
            if (!record.get(9).isNull()) {
                Collections.addAll(languages, record.get(9).asString().split(","));
            }

            List<LdbcQuery1Result.Organization> universities = new ArrayList<>();
            if (!record.get(11).isNull()) {
                List<ValueWrapper> value = record.get(11).asList();
                for (ValueWrapper list : value) {
                    List<ValueWrapper> res = list.asList();
                    universities.add(new LdbcQuery1Result.Organization(res.get(0).asString(), ((Long)res.get(1).asLong()).intValue(), res.get(2).asString()));
                }
            }

            List<LdbcQuery1Result.Organization>companies = new ArrayList<>();
            if (!record.get(12).isNull()) {
                List<ValueWrapper> value = record.get(12).asList();
                for (ValueWrapper list : value) {
                    List<ValueWrapper> res = list.asList();
                    companies.add(new LdbcQuery1Result.Organization(res.get(0).asString(), ((Long)res.get(1).asLong()).intValue(), res.get(2).asString()));
                }
            }

            long friendId = record.get(0).asLong();
            String friendLastName = record.get(1).asString();
            int distanceFromPerson = (int) record.get(2).asLong();
            long friendBirthday = NebulaConverter.convertDateToEpoch(record.get(3).asString());
            long friendCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(4).asDateTime().getLocalDateTimeStr());
            String friendGender = record.get(5).asString();
            String friendBrowserUsed = record.get(6).asString();
            String friendLocationIp = record.get(7).asString();
            String friendCityName = record.get(10).asString();
            return new LdbcQuery1Result(
                    friendId,
                    friendLastName,
                    distanceFromPerson,
                    friendBirthday,
                    friendCreationDate,
                    friendGender,
                    friendBrowserUsed,
                    friendLocationIp,
                    emails,
                    languages,
                    friendCityName,
                    universities,
                    companies);
        }
    }


    public static class InteractiveQuery2 extends NebulaListOperationHandler<LdbcQuery2, LdbcQuery2Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery2 operation) {
            return state.getQueryStore().getQuery2(operation);
        }

        @Override
        public LdbcQuery2Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            long messageId = record.get(3).asLong();
            String messageContent = record.get(4).asString();
            long messageCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(5).asDateTime().getLocalDateTimeStr());

            return new LdbcQuery2Result(
                    personId,
                    personFirstName,
                    personLastName,
                    messageId,
                    messageContent,
                    messageCreationDate);
        }
    }

    public static class InteractiveQuery3 extends NebulaListOperationHandler<LdbcQuery3, LdbcQuery3Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery3 operation) {
            return state.getQueryStore().getQuery3(operation);
        }

        @Override
        public LdbcQuery3Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            int xCount = (int) record.get(3).asLong();
            int yCount = (int) record.get(4).asLong();
            int count = (int) record.get(5).asLong();
            return new LdbcQuery3Result(
                    personId,
                    personFirstName,
                    personLastName,
                    xCount,
                    yCount,
                    count);
        }
    }

    public static class InteractiveQuery4 extends NebulaListOperationHandler<LdbcQuery4, LdbcQuery4Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery4 operation) {
            return state.getQueryStore().getQuery4(operation);
        }

        @Override
        public LdbcQuery4Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            String tagName = record.get(0).asString();
            int postCount = (int) record.get(1).asLong();
            return new LdbcQuery4Result(tagName, postCount);
        }

    }

    public static class InteractiveQuery5 extends NebulaListOperationHandler<LdbcQuery5, LdbcQuery5Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery5 operation) {
            return state.getQueryStore().getQuery5(operation);
        }

        @Override
        public LdbcQuery5Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            String forumTitle = record.get(1).asString();
            int postCount = (int) record.get(2).asLong();
            return new LdbcQuery5Result(forumTitle, postCount);
        }
    }

    public static class InteractiveQuery6 extends NebulaListOperationHandler<LdbcQuery6, LdbcQuery6Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery6 operation) {
            return state.getQueryStore().getQuery6(operation);
        }

        @Override
        public LdbcQuery6Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            String tagName = record.get(0).asString();
            int postCount = (int) record.get(1).asLong();
            return new LdbcQuery6Result(tagName, postCount);
        }
    }

    public static class InteractiveQuery7 extends NebulaListOperationHandler<LdbcQuery7, LdbcQuery7Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery7 operation) {
            return state.getQueryStore().getQuery7(operation);
        }

        @Override
        public LdbcQuery7Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            long likeCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(3).asDateTime().getLocalDateTimeStr());
            long messageId = Long.parseLong(record.get(4).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String messageContent = record.get(5).asString();
            int minutesLatency = NebulaConverter.convertStartAndEndDateToLatency(
                    record.get(6).asDateTime().getLocalDateTimeStr(), record.get(3).asDateTime().getLocalDateTimeStr());
            boolean isNew = record.get(7).asBoolean();
            return new LdbcQuery7Result(
                    personId,
                    personFirstName,
                    personLastName,
                    likeCreationDate,
                    messageId,
                    messageContent,
                    minutesLatency,
                    isNew);
        }
    }

    public static class InteractiveQuery8 extends NebulaListOperationHandler<LdbcQuery8, LdbcQuery8Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery8 operation) {
            return state.getQueryStore().getQuery8(operation);
        }

        @Override
        public LdbcQuery8Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long personId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            long commentCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(3).asDateTime().getLocalDateTimeStr());
            long commentId = record.get(4).asLong();
            String commentContent = record.get(5).asString();
            return new LdbcQuery8Result(
                    personId,
                    personFirstName,
                    personLastName,
                    commentCreationDate,
                    commentId,
                    commentContent);
        }
    }

    public static class InteractiveQuery9 extends NebulaListOperationHandler<LdbcQuery9, LdbcQuery9Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery9 operation) {
            return state.getQueryStore().getQuery9(operation);
        }

        @Override
        public LdbcQuery9Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long personId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            long messageId = record.get(3).asLong();
            String messageContent = record.get(4).asString();
            long messageCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(5).asDateTime().getLocalDateTimeStr());
            return new LdbcQuery9Result(
                    personId,
                    personFirstName,
                    personLastName,
                    messageId,
                    messageContent,
                    messageCreationDate);
        }
    }

    public static class InteractiveQuery10 extends NebulaListOperationHandler<LdbcQuery10, LdbcQuery10Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery10 operation) {
            return state.getQueryStore().getQuery10(operation);
        }

        @Override
        public LdbcQuery10Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            int commonInterestScore = (int) record.get(3).asLong();
            String personGender = record.get(4).asString();
            String personCityName = record.get(5).asString();
            return new LdbcQuery10Result(
                    personId,
                    personFirstName,
                    personLastName,
                    commonInterestScore,
                    personGender,
                    personCityName);
        }
    }

    public static class InteractiveQuery11 extends NebulaListOperationHandler<LdbcQuery11, LdbcQuery11Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery11 operation) {
            return state.getQueryStore().getQuery11(operation);
        }

        @Override
        public LdbcQuery11Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException  {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            String organizationName = record.get(3).asString();
            int organizationWorkFromYear = (int) record.get(4).asLong();
            return new LdbcQuery11Result(
                    personId,
                    personFirstName,
                    personLastName,
                    organizationName,
                    organizationWorkFromYear);
        }
    }

    public static class InteractiveQuery12 extends NebulaListOperationHandler<LdbcQuery12, LdbcQuery12Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery12 operation) {
            return state.getQueryStore().getQuery12(operation);
        }

        @Override
        public LdbcQuery12Result convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException  {
            long personId = record.get(0).asLong();
            String personFirstName = record.get(1).asString();
            String personLastName = record.get(2).asString();
            List<String> tagNames = new ArrayList<>();
            if (!record.get(3).isNull()) {
                ArrayList<ValueWrapper> values = record.get(3).asList();
                for (ValueWrapper val : values) {
                    tagNames.add(val.asString());
                }
            }
            int replyCount = (int) record.get(4).asLong();
            return new LdbcQuery12Result(
                    personId,
                    personFirstName,
                    personLastName,
                    tagNames,
                    replyCount);
        }
    }

    public static class InteractiveQuery13 extends NebulaSingletonOperationHandler<LdbcQuery13, LdbcQuery13Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery13 operation) {
            return state.getQueryStore().getQuery13(operation);
        }

        @Override
        public LdbcQuery13Result convertSingleResult(ResultSet.Record record) {
            return new LdbcQuery13Result((int) record.get(0).asLong());
        }

        @Override
        public LdbcQuery13Result noResult() {
            return new LdbcQuery13Result(-1);
        }
    }

    public static class InteractiveQuery14 extends NebulaListOperationHandler<LdbcQuery14, LdbcQuery14Result> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcQuery14 operation) {
            return state.getQueryStore().getQuery14(operation);
        }

        @Override
        public LdbcQuery14Result convertSingleResult(ResultSet.Record record) {
            List<Long> personIdsInPath = new ArrayList<>();
            if (!record.get(0).isNull()) {
                // TODO: personIdsInPath = record.get(0).asList((e) -> e.asLong());
            }
            double pathWight = record.get(1).asDouble();
            return new LdbcQuery14Result(
                    personIdsInPath,
                    pathWight);
        }
    }

    // Interactive short reads

    public static class ShortQuery1PersonProfile extends NebulaSingletonOperationHandler<LdbcShortQuery1PersonProfile, LdbcShortQuery1PersonProfileResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery1PersonProfile operation) {
            return state.getQueryStore().getShortQuery1PersonProfile(operation);
        }

        @Override
        public LdbcShortQuery1PersonProfileResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            String firstName = record.get(0).asString();
            String lastName = record.get(1).asString();
            long birthday = NebulaConverter.convertDateToEpoch(record.get(2).asString());
            String locationIP = record.get(3).asString();
            String browserUsed = record.get(4).asString();
            long cityId = Long.parseLong(record.get(5).asString().substring(NebulaID.ID_PREFIX_SIZE));

            String gender = record.get(6).asString();
            long creationDate = NebulaConverter.convertDateTimesToEpoch(record.get(7).asDateTime().getLocalDateTimeStr());
            return new LdbcShortQuery1PersonProfileResult(
                    firstName,
                    lastName,
                    birthday,
                    locationIP,
                    browserUsed,
                    cityId,
                    gender,
                    creationDate);
        }

        @Override
        public LdbcShortQuery1PersonProfileResult noResult() {
            return new LdbcShortQuery1PersonProfileResult(
                    "",
                    "",
                    0,
                    "",
                    "",
                    0,
                    "",
                    0);
        }
    }

    public static class ShortQuery2PersonPosts extends NebulaListOperationHandler<LdbcShortQuery2PersonPosts, LdbcShortQuery2PersonPostsResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery2PersonPosts operation) {
            return state.getQueryStore().getShortQuery2PersonPosts(operation);
        }

        @Override
        public LdbcShortQuery2PersonPostsResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long messageId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String messageContent = record.get(1).asString();
            long messageCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(2).asDateTime().getLocalDateTimeStr());
            long originalPostId = Long.parseLong(record.get(3).asString().substring(NebulaID.ID_PREFIX_SIZE));
            long originalPostAuthorId = Long.parseLong(record.get(4).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String originalPostAuthorFirstName = record.get(5).asString();
            String originalPostAuthorLastName = record.get(6).asString();
            return new LdbcShortQuery2PersonPostsResult(
                    messageId,
                    messageContent,
                    messageCreationDate,
                    originalPostId,
                    originalPostAuthorId,
                    originalPostAuthorFirstName,
                    originalPostAuthorLastName);
        }
    }

    public static class ShortQuery3PersonFriends extends NebulaListOperationHandler<LdbcShortQuery3PersonFriends, LdbcShortQuery3PersonFriendsResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery3PersonFriends operation) {
            return state.getQueryStore().getShortQuery3PersonFriends(operation);
        }

        @Override
        public LdbcShortQuery3PersonFriendsResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long personId = record.get(0).asLong();
            String firstName = record.get(1).asString();
            String lastName = record.get(2).asString();
            long friendshipCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(3).asDateTime().getLocalDateTimeStr());
            return new LdbcShortQuery3PersonFriendsResult(
                    personId,
                    firstName,
                    lastName,
                    friendshipCreationDate);
        }
    }

    public static class ShortQuery4MessageContent extends NebulaSingletonOperationHandler<LdbcShortQuery4MessageContent, LdbcShortQuery4MessageContentResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery4MessageContent operation) {
            return state.getQueryStore().getShortQuery4MessageContent(operation);
        }

        @Override
        public LdbcShortQuery4MessageContentResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long messageCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(0).asDateTime().getLocalDateTimeStr());
            String messageContent = record.get(1).asString();
            return new LdbcShortQuery4MessageContentResult(
                    messageContent,
                    messageCreationDate);
        }

        @Override
        public LdbcShortQuery4MessageContentResult noResult() {
            return new LdbcShortQuery4MessageContentResult(
                    "",
                    0);
        }
    }

    public static class ShortQuery5MessageCreator extends NebulaSingletonOperationHandler<LdbcShortQuery5MessageCreator, LdbcShortQuery5MessageCreatorResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery5MessageCreator operation) {
            return state.getQueryStore().getShortQuery5MessageCreator(operation);
        }

        @Override
        public LdbcShortQuery5MessageCreatorResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            long personId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String firstName = record.get(1).asString();
            String lastName = record.get(2).asString();
            return new LdbcShortQuery5MessageCreatorResult(
                    personId,
                    firstName,
                    lastName);
        }

        @Override
        public LdbcShortQuery5MessageCreatorResult noResult() {
            return new LdbcShortQuery5MessageCreatorResult(
                    0,
                    "",
                    "");
        }
    }

    public static class ShortQuery6MessageForum extends NebulaSingletonOperationHandler<LdbcShortQuery6MessageForum, LdbcShortQuery6MessageForumResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery6MessageForum operation) {
            return state.getQueryStore().getShortQuery6MessageForum(operation);
        }

        @Override
        public LdbcShortQuery6MessageForumResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException {
            long forumId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String forumTitle = record.get(1).asString();
            long moderatorId = Long.parseLong(record.get(2).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String moderatorFirstName = record.get(3).asString();
            String moderatorLastName = record.get(4).asString();
            return new LdbcShortQuery6MessageForumResult(
                    forumId,
                    forumTitle,
                    moderatorId,
                    moderatorFirstName,
                    moderatorLastName);
        }

        @Override
        public LdbcShortQuery6MessageForumResult noResult() {
            return new LdbcShortQuery6MessageForumResult(
                    0,
                    "",
                    0,
                    "",
                    "");
        }
    }

    public static class ShortQuery7MessageReplies extends NebulaListOperationHandler<LdbcShortQuery7MessageReplies, LdbcShortQuery7MessageRepliesResult> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcShortQuery7MessageReplies operation) {
            return state.getQueryStore().getShortQuery7MessageReplies(operation);
        }

        @Override
        public LdbcShortQuery7MessageRepliesResult convertSingleResult(ResultSet.Record record) throws UnsupportedEncodingException, ParseException {
            long commentId = Long.parseLong(record.get(0).asString().substring(NebulaID.ID_PREFIX_SIZE));
            String commentContent = record.get(1).asString();
            long commentCreationDate = NebulaConverter.convertDateTimesToEpoch(record.get(2).asDateTime().getLocalDateTimeStr());
            long replyAuthorId = record.get(3).asLong();
            String replyAuthorFirstName = record.get(4).asString();
            String replyAuthorLastName = record.get(5).asString();
            boolean replyAuthorKnowsOriginalMessageAuthor = record.get(6).asBoolean();
            return new LdbcShortQuery7MessageRepliesResult(
                    commentId,
                    commentContent,
                    commentCreationDate,
                    replyAuthorId,
                    replyAuthorFirstName,
                    replyAuthorLastName,
                    replyAuthorKnowsOriginalMessageAuthor);
        }
    }

    // Interactive updates
    
    public static class Update1AddPerson extends  NebulaMultipleUpdateOperationHandler<LdbcUpdate1AddPerson> {

        @Override
        public List<String> getQueryString(NebulaDbConnectionState state, LdbcUpdate1AddPerson operation) {
            return state.getQueryStore().getUpdate1Multiple(operation);
        }
    }

    public static class Update2AddPostLike extends NebulaUpdateOperationHandler<LdbcUpdate2AddPostLike> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcUpdate2AddPostLike operation) {
            return state.getQueryStore().getUpdate2(operation);
        }
    }

    public static class Update3AddCommentLike extends NebulaUpdateOperationHandler<LdbcUpdate3AddCommentLike> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcUpdate3AddCommentLike operation) {
            return state.getQueryStore().getUpdate3(operation);
        }
    }

    public static class Update4AddForum extends NebulaMultipleUpdateOperationHandler<LdbcUpdate4AddForum> {

        @Override
        public List<String> getQueryString(NebulaDbConnectionState state, LdbcUpdate4AddForum operation) {
            return state.getQueryStore().getUpdate4Multiple(operation);
        }
    }

    public static class Update5AddForumMembership extends NebulaUpdateOperationHandler<LdbcUpdate5AddForumMembership> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcUpdate5AddForumMembership operation) {
            return state.getQueryStore().getUpdate5(operation);
        }
    }

    public static class Update6AddPost extends NebulaMultipleUpdateOperationHandler<LdbcUpdate6AddPost> {

        @Override
        public List<String> getQueryString(NebulaDbConnectionState state, LdbcUpdate6AddPost operation) {
            return state.getQueryStore().getUpdate6Multiple(operation);
        }
    }

    public static class Update7AddComment extends NebulaMultipleUpdateOperationHandler<LdbcUpdate7AddComment> {

        @Override
        public List<String> getQueryString(NebulaDbConnectionState state, LdbcUpdate7AddComment operation) {
            return state.getQueryStore().getUpdate7Multiple(operation);
        }
    }

    public static class Update8AddFriendship extends NebulaUpdateOperationHandler<LdbcUpdate8AddFriendship> {

        @Override
        public String getQueryString(NebulaDbConnectionState state, LdbcUpdate8AddFriendship operation) {
            return state.getQueryStore().getUpdate8(operation);
        }
    }
}
