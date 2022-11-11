package org.ldbcouncil.snb.impls.workloads.nebula;

import com.google.common.collect.ImmutableMap;
import org.ldbcouncil.snb.driver.DbException;
import org.ldbcouncil.snb.driver.workloads.interactive.*;
import org.ldbcouncil.snb.impls.workloads.QueryStore;
import org.ldbcouncil.snb.impls.workloads.converter.Converter;
import org.ldbcouncil.snb.impls.workloads.nebula.converter.NebulaConverter;
import org.ldbcouncil.snb.impls.workloads.QueryType;

import java.util.Calendar;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class NebulaQueryStore extends QueryStore {
    public static final String COMMENT_ID = "commentId";

    public NebulaQueryStore(String path) throws DbException  {
        super(path, ".ngql");
    }

    @Override
    public Converter getConverter() {
        return new NebulaConverter();
    }

    @Override
    public Map<String, Object> getQuery1Map(LdbcQuery1 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ1())))
                .put("firstName", getConverter().convertString(operation.getFirstName()))
                .build();
    }

    

    public Map<String, Object> getQuery2Map(LdbcQuery2 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ2())))
                .put("maxDate", getConverter().convertDateTime(operation.getMaxDate()))
                .build();
    }

    protected Date addDays(Date startDate, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    @Override
    public Map<String, Object> getQuery3Map(LdbcQuery3 operation) {
        Date endDate = addDays(operation.getStartDate(), operation.getDurationDays());
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ3())))
                .put("countryXName", getConverter().convertString(operation.getCountryXName()))
                .put("countryYName", getConverter().convertString(operation.getCountryYName()))
                .put("startDate", getConverter().convertDateTime(operation.getStartDate()))
                .put("durationDays", getConverter().convertInteger(operation.getDurationDays()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery4Map(LdbcQuery4 operation) {
        Date endDate = addDays(operation.getStartDate(), operation.getDurationDays());
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ4())))
                .put("startDate", getConverter().convertDateTime(operation.getStartDate()))
                .put("endDate", getConverter().convertDateTime(endDate))
                .build();
    }

    @Override
    public Map<String, Object> getQuery5Map(LdbcQuery5 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ5())))
                .put("minDate", getConverter().convertDateTime(operation.getMinDate()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery6Map(LdbcQuery6 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ6())))
                .put("tagName", getConverter().convertString(operation.getTagName()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery7Map(LdbcQuery7 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ7())))
                .build();
    }


    @Override
    public Map<String, Object> getQuery8Map(LdbcQuery8 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ8())))
                .build();
    }

    @Override
    public Map<String, Object> getQuery9Map(LdbcQuery9 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ9())))
                .put("maxDate", getConverter().convertDateTime(operation.getMaxDate()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery10Map(LdbcQuery10 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ10())))
                .put("month", getConverter().convertInteger(operation.getMonth()))
                .put("nextMonth", getConverter().convertInteger(operation.getMonth() % 12 + 1))
                .build();
    }

    @Override
    public Map<String, Object> getQuery11Map(LdbcQuery11 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ11())))
                .put("countryName", getConverter().convertString(operation.getCountryName()))
                .put("workFromYear", getConverter().convertInteger(operation.getWorkFromYear()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery12Map(LdbcQuery12 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdQ12())))
                .put("tagClassName", getConverter().convertString(operation.getTagClassName()))
                .build();
    }

    @Override
    public Map<String, Object> getQuery13Map(LdbcQuery13 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("person1Id", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson1IdQ13StartNode())))
                .put("person2Id", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson2IdQ13EndNode())))
                .build();
    }

    @Override
    public Map<String, Object> getQuery14Map(LdbcQuery14 operation) {
        return new ImmutableMap.Builder<String, Object>()
                .put("person1Id", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson1IdQ14StartNode())))
                .put("person2Id", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson2IdQ14EndNode())))
                .build();
    }

    // Interactive short reads

    @Override
    public Map<String, Object> getShortQuery1PersonProfileMap(LdbcShortQuery1PersonProfile operation) {
        return ImmutableMap.of(
                        LdbcShortQuery1PersonProfile.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdSQ1()))
        );
    }


    @Override
    public Map<String, Object> getShortQuery2PersonPostsMap(LdbcShortQuery2PersonPosts operation) {
        return ImmutableMap.of(LdbcShortQuery2PersonPosts.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdSQ2()))
        );
    }

    @Override
    public Map<String, Object> getShortQuery3PersonFriendsMap(LdbcShortQuery3PersonFriends operation) {
        return ImmutableMap.of(LdbcShortQuery3PersonFriends.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonIdSQ3()))
        );
    }

    @Override
    public Map<String, Object> getShortQuery4MessageContentMap(LdbcShortQuery4MessageContent operation) {
        return ImmutableMap.of(COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getMessageIdContent())),
                        "postId", getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getMessageIdContent()))
        );
    }

    @Override
    public Map<String, Object> getShortQuery5MessageCreatorMap(LdbcShortQuery5MessageCreator operation) {
        return ImmutableMap.of(COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getMessageIdCreator())),
                                "postId", getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getMessageIdCreator()))
        );
    }

    @Override
    public Map<String, Object> getShortQuery6MessageForumMap(LdbcShortQuery6MessageForum operation) {
        return ImmutableMap.of(COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getMessageForumId())),
                                "postId", getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getMessageForumId()))

        );
    }

    @Override
    public Map<String, Object> getShortQuery7MessageRepliesMap(LdbcShortQuery7MessageReplies operation) {
        return ImmutableMap.of(COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getMessageRepliesId())),
                        "postId", getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getMessageRepliesId()))
        );
    }

    @Override
    public String getUpdate2(LdbcUpdate2AddPostLike operation) {
        return prepare(
                QueryType.InteractiveUpdate2,
                ImmutableMap.of(
                        LdbcUpdate2AddPostLike.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonId())),
                        LdbcUpdate2AddPostLike.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getPostId())),
                        LdbcUpdate2AddPostLike.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    @Override
    public String getUpdate3(LdbcUpdate3AddCommentLike operation) {
        return prepare(
                QueryType.InteractiveUpdate3,
                ImmutableMap.of(
                        LdbcUpdate3AddCommentLike.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonId())),
                        LdbcUpdate3AddCommentLike.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getCommentId())),
                        LdbcUpdate3AddCommentLike.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    @Override
    public String getUpdate5(LdbcUpdate5AddForumMembership operation) {
        return prepare(
                QueryType.InteractiveUpdate5,
                ImmutableMap.of(
                        LdbcUpdate5AddForumMembership.FORUM_ID, getConverter().convertString(NebulaID.FORUM_ID_PREFIX + getConverter().convertId(operation.getForumId())),
                        LdbcUpdate5AddForumMembership.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPersonId())),
                        LdbcUpdate5AddForumMembership.JOIN_DATE, getConverter().convertDateTime(operation.getJoinDate())
                )
        );
    }

    @Override
    public String getUpdate8(LdbcUpdate8AddFriendship operation) {
        return prepare(
                QueryType.InteractiveUpdate8,
                ImmutableMap.of(
                        LdbcUpdate8AddFriendship.PERSON1_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson1Id())),
                        LdbcUpdate8AddFriendship.PERSON2_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getPerson2Id())),
                        LdbcUpdate8AddFriendship.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        );
    }

    // multiple
    @Override
    public List<String> getUpdate1Multiple(LdbcUpdate1AddPerson operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate1AddPerson,
                new ImmutableMap.Builder<String, Object>()
                        .put(LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPersonId())))
                        .put(LdbcUpdate1AddPerson.PERSON_FIRST_NAME, getConverter().convertString(operation.getPersonFirstName()))
                        .put(LdbcUpdate1AddPerson.PERSON_LAST_NAME, getConverter().convertString(operation.getPersonLastName()))
                        .put(LdbcUpdate1AddPerson.GENDER, getConverter().convertString(operation.getGender()))
                        .put(LdbcUpdate1AddPerson.BIRTHDAY, NebulaConverter.convertDateToStr(operation.getBirthday()))
                        .put(LdbcUpdate1AddPerson.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate1AddPerson.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate1AddPerson.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .build()
        ));

        for (LdbcUpdate1AddPerson.Organization organization : operation.getWorkAt()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonCompanies,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPersonId())),
                            "organizationId", getConverter().convertString(NebulaID.ORGANISATION_ID_PREFIX + getConverter().convertId(organization.getOrganizationId())),
                            "worksFromYear", getConverter().convertInteger(organization.getYear())
                    )
            ));
        }

        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonTags,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPersonId())),
                            "tagId", getConverter().convertString(NebulaID.TAG_ID_PREFIX + getConverter().convertId(tagId)))
                    )
            );
        }
        for (LdbcUpdate1AddPerson.Organization organization : operation.getStudyAt()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate1AddPersonUniversities,
                    ImmutableMap.of(
                            LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPersonId())),
                            "organizationId", getConverter().convertString(NebulaID.ORGANISATION_ID_PREFIX + getConverter().convertId(organization.getOrganizationId())),
                            "studiesFromYear", getConverter().convertInteger(organization.getYear())
                    )
            ));
        }
        // add person->Place(city)
        list.add(prepare(
                QueryType.InteractiveUpdate1AddPersonPlace,
                ImmutableMap.of(
                        LdbcUpdate1AddPerson.PERSON_ID, getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPersonId())),
                        "cityId", getConverter().convertString(NebulaID.PLACE_ID_PREFIX + getConverter().convertId(operation.getCityId())))
                        ));
        return list;
    }

    @Override
    public List<String> getUpdate4Multiple(LdbcUpdate4AddForum operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate4AddForum,
                ImmutableMap.of(
                        LdbcUpdate4AddForum.FORUM_ID, getConverter().convertString(NebulaID.FORUM_ID_PREFIX + getConverter().convertIdForInsertion(operation.getForumId())),
                        LdbcUpdate4AddForum.FORUM_TITLE, getConverter().convertString(operation.getForumTitle()),
                        LdbcUpdate4AddForum.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate())
                )
        ));

        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate4AddForumTags,
                    ImmutableMap.of(
                            LdbcUpdate4AddForum.FORUM_ID, getConverter().convertString(NebulaID.FORUM_ID_PREFIX + getConverter().convertIdForInsertion(operation.getForumId())),
                            "tagId", getConverter().convertString(NebulaID.TAG_ID_PREFIX + getConverter().convertId(tagId)))
                    )
            );
        }
        // add Forum-[hasModerator]->Person
        list.add(prepare(
                QueryType.InteractiveUpdate4AddForumPerson,
                ImmutableMap.of(
                        LdbcUpdate4AddForum.FORUM_ID, getConverter().convertString(NebulaID.FORUM_ID_PREFIX + getConverter().convertIdForInsertion(operation.getForumId())),
                        "moderatorPersonId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getModeratorPersonId())))
        ));
        return list;

    }

    @Override
    public List<String> getUpdate6Multiple(LdbcUpdate6AddPost operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPost,
                new ImmutableMap.Builder<String, Object>()
                        .put(LdbcUpdate6AddPost.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPostId())))
                        .put(LdbcUpdate6AddPost.IMAGE_FILE, getConverter().convertString(operation.getImageFile()))
                        .put(LdbcUpdate6AddPost.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate6AddPost.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate6AddPost.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate6AddPost.LANGUAGE, getConverter().convertString(operation.getLanguage()))
                        .put(LdbcUpdate6AddPost.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate6AddPost.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .build()
                )
        );
        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate6AddPostTags,
                    ImmutableMap.of(
                            LdbcUpdate6AddPost.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPostId())),
                            "tagId", getConverter().convertString(NebulaID.TAG_ID_PREFIX + getConverter().convertId(tagId)))
                    )
            );
        }

        // add Post-[isLocalIn]->Place(Country)
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPostPlace,
                ImmutableMap.of(
                        LdbcUpdate6AddPost.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPostId())),
                        "countryId", getConverter().convertString(NebulaID.PLACE_ID_PREFIX + getConverter().convertId(operation.getCountryId())))
        ));

        // add Post-[hasCreator]->Person
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPostPerson,
                ImmutableMap.of(
                        LdbcUpdate6AddPost.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPostId())),
                        "authorPersonId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getAuthorPersonId())))
        ));

        // add Post-[containerOf]->Forum
        list.add(prepare(
                QueryType.InteractiveUpdate6AddPostForum,
                ImmutableMap.of(
                        LdbcUpdate6AddPost.POST_ID, getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertIdForInsertion(operation.getPostId())),
                        "forumId", getConverter().convertString(NebulaID.FORUM_ID_PREFIX + getConverter().convertId(operation.getForumId())))
        ));

        return list;
    }

    @Override
    public List<String> getUpdate7Multiple(LdbcUpdate7AddComment operation) {
        List<String> list = new ArrayList<>();
        list.add(prepare(
                QueryType.InteractiveUpdate7AddComment,
                new ImmutableMap.Builder<String, Object>()
                        .put(LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())))
                        .put(LdbcUpdate7AddComment.CREATION_DATE, getConverter().convertDateTime(operation.getCreationDate()))
                        .put(LdbcUpdate7AddComment.LOCATION_IP, getConverter().convertString(operation.getLocationIp()))
                        .put(LdbcUpdate7AddComment.BROWSER_USED, getConverter().convertString(operation.getBrowserUsed()))
                        .put(LdbcUpdate7AddComment.CONTENT, getConverter().convertString(operation.getContent()))
                        .put(LdbcUpdate7AddComment.LENGTH, getConverter().convertInteger(operation.getLength()))
                        .build()
        ));
        for (long tagId : operation.getTagIds()) {
            list.add(prepare(
                    QueryType.InteractiveUpdate7AddCommentTags,
                    ImmutableMap.of(
                            LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())),
                            "tagId", getConverter().convertString(NebulaID.TAG_ID_PREFIX + getConverter().convertId(tagId)))
                    )
            );
        }

        // add Comment-[Comment_reply_Of]->Comment
        if (operation.getReplyToCommentId() != -1) {
            list.add(prepare(
                    QueryType.InteractiveUpdate7AddCommentComment,
                    ImmutableMap.of(
                            LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())),
                            "replyToCommentId", getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertId(operation.getReplyToCommentId())))
            ));
        }

        // add Comment-[Post_reply_Of]->Post
        if (operation.getReplyToPostId() != -1) {
            list.add(prepare(
                    QueryType.InteractiveUpdate7AddCommentPost,
                    ImmutableMap.of(
                            LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())),
                            "replyToPostId", getConverter().convertString(NebulaID.POST_ID_PREFIX + getConverter().convertId(operation.getReplyToPostId())))
            ));
        }

        // add Comment-[isLocatedIn]->Place(Country)
        list.add(prepare(
                QueryType.InteractiveUpdate7AddCommentPlace,
                ImmutableMap.of(
                        LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())),
                        "countryId", getConverter().convertString(NebulaID.PLACE_ID_PREFIX + getConverter().convertId(operation.getCountryId())))
        ));

        // add Comment-[hasCreator]->Person
        list.add(prepare(
                QueryType.InteractiveUpdate7AddCommentPerson,
                ImmutableMap.of(
                        LdbcUpdate7AddComment.COMMENT_ID, getConverter().convertString(NebulaID.COMMENT_ID_PREFIX + getConverter().convertIdForInsertion(operation.getCommentId())),
                        "personId", getConverter().convertString(NebulaID.PERSON_ID_PREFIX + getConverter().convertId(operation.getAuthorPersonId())))
        ));

        return list;
    }

}
