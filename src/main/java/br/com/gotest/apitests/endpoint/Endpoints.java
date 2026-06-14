package br.com.gotest.apitests.endpoint;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Endpoints {

    // ---- Auth ----
    public static final String AUTH_GOOGLE_USERINFO = "/auth/google/userinfo";
    public static final String AUTH_GOOGLE_CALLBACK = "/auth/google/callback";

    // ---- Users ----
    public static final String USERS_ME = "/users/me";
    public static final String USERS_NOTIFICATIONS_UNREAD_ACCOUNTANT = "/users/notifications/unread/accountant";
    public static final String USERS_NOTIFICATIONS_RECENT = "/users/notifications/recent";
    public static final String USERS_ME_EXECUTION_PREFERENCES = "/users/me/execution-preferences";

    // ---- Projects ----
    public static final String PROJECTS = "/projects";
    public static final String PROJECTS_ALL = "/projects/all";
    public static final String PROJECTS_FILTER = "/projects/filter";

    public static String projectById(String projectId) {
        return PROJECTS + "/" + projectId;
    }

    public static String projectOwnerIdentifier(String ownerId) {
        return PROJECTS + "/owners/" + ownerId + "/identifier";
    }

    public static String projectOwnerIdentifierValidate(String ownerId) {
        return projectOwnerIdentifier(ownerId) + "/validate";
    }

    // ---- Project Members ----
    public static String projectMembers(String projectId) {
        return projectById(projectId) + "/members";
    }

    public static String projectMembersFilter(String projectId) {
        return projectMembers(projectId) + "/filter";
    }

    public static String projectMembersSuggestion(String projectId) {
        return projectMembers(projectId) + "/suggestion";
    }

    public static String projectMembersValidate(String projectId) {
        return projectMembers(projectId) + "/validate";
    }

    // ---- Project Stats ----
    public static String projectPlansEfficiency(String projectId) {
        return projectById(projectId) + "/plans/efficiency";
    }

    public static String projectPlansSummary(String projectId) {
        return projectById(projectId) + "/plans/summary";
    }

    // ---- Nodes ----
    public static String projectNodes(String projectId) {
        return projectById(projectId) + "/nodes";
    }

    public static String projectNodesAll(String projectId) {
        return projectNodes(projectId) + "/all";
    }

    public static String projectNodesFilter(String projectId) {
        return projectNodes(projectId) + "/filter";
    }

    public static String projectNodesAllForSelection(String projectId) {
        return projectNodes(projectId) + "/all-for-selection";
    }

    public static String projectNodesFolders(String projectId) {
        return projectNodes(projectId) + "/folders";
    }

    public static String projectNodeById(String projectId, String nodeId) {
        return projectNodes(projectId) + "/" + nodeId;
    }

    // ---- Suites (nó do projeto) ----
    public static String projectNodesSuites(String projectId) {
        return projectNodes(projectId) + "/suites";
    }

    public static String projectNodesSuitesNextIdentifier(String projectId) {
        return projectNodesSuites(projectId) + "/nextIdentifier";
    }

    // ---- Tests (filhos de suite) ----
    public static String suiteTests(String projectId, String suiteId) {
        return projectById(projectId) + "/suites/" + suiteId + "/tests";
    }

    public static String suiteTestsAll(String projectId, String suiteId) {
        return suiteTests(projectId, suiteId) + "/all";
    }

    public static String suiteTestsNextIdentifier(String projectId, String suiteId) {
        return suiteTests(projectId, suiteId) + "/nextIdentifier";
    }

    public static String suiteTestsValidateTitle(String projectId, String suiteId) {
        return suiteTests(projectId, suiteId) + "/validate-title";
    }

    public static String suiteTestsFilter(String projectId, String suiteId) {
        return suiteTests(projectId, suiteId) + "/filter";
    }

    public static String suiteTestById(String projectId, String suiteId, String testId) {
        return suiteTests(projectId, suiteId) + "/" + testId;
    }

    public static String suiteTestAiReview(String projectId, String suiteId, String testId) {
        return suiteTestById(projectId, suiteId, testId) + "/ai/review";
    }

    public static String suiteTestAiReviews(String projectId, String suiteId, String testId) {
        return suiteTestById(projectId, suiteId, testId) + "/ai/reviews";
    }

    // ---- Plans ----
    public static String projectPlans(String projectId) {
        return projectById(projectId) + "/plans";
    }

    public static String projectPlansAll(String projectId) {
        return projectPlans(projectId) + "/all";
    }

    public static String projectPlansValidateName(String projectId) {
        return projectPlans(projectId) + "/validate-name";
    }

    public static String projectPlansFilter(String projectId) {
        return projectPlans(projectId) + "/filter";
    }

    public static String projectPlansRelations(String projectId) {
        return projectPlans(projectId) + "/relations";
    }

    public static String projectPlanById(String projectId, String planId) {
        return projectPlans(projectId) + "/" + planId;
    }

    public static String projectPlanDashboard(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/dashboard";
    }

    public static String projectPlanExecutionsCheck(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/executions/check";
    }

    public static String projectPlanChartsLast(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/charts/last";
    }

    public static String projectPlanChartsRecent(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/charts/recent";
    }

    public static String projectPlanFlatEntities(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/flat-entities";
    }

    public static String projectPlanFolder(String projectId, String planId, String folderId) {
        return projectPlanById(projectId, planId) + "/folder/" + folderId;
    }

    public static String projectPlanSuiteTests(String projectId, String planId, String suiteId) {
        return projectPlanById(projectId, planId) + "/suites/" + suiteId + "/tests";
    }

    // ---- Executions ----
    public static String planExecutions(String projectId, String planId) {
        return projectPlanById(projectId, planId) + "/executions";
    }

    public static String executionById(String projectId, String executionId) {
        return projectById(projectId) + "/plans/executions/" + executionId;
    }

    public static String executionInitialize(String projectId, String executionId) {
        return executionById(projectId, executionId) + "/initialize";
    }

    public static String testExecutionById(String projectId, String testExecutionId) {
        return projectById(projectId) + "/plans/test-executions/" + testExecutionId;
    }

    // ---- Access Management ----
    public static final String ACCESS_MANAGEMENT_LIST_ROLES = "/accessManagement/listRoles";

    // ---- DELETE paths (seguindo convenção REST) ----
    public static String deleteProject(String projectId) {
        return projectById(projectId);
    }

    public static String deleteNode(String projectId, String nodeId) {
        return projectNodeById(projectId, nodeId);
    }

    public static String deleteSuiteTest(String projectId, String suiteId, String testId) {
        return suiteTestById(projectId, suiteId, testId);
    }

    public static String deletePlan(String projectId, String planId) {
        return projectPlanById(projectId, planId);
    }
}
