package de.araneaconsult;

import java.io.IOException;

import de.araneaconsult.idm.oauth.OAuth;
import de.araneaconsult.idm.oauth.ResponseException;
import de.araneaconsult.idm.oauth.Token;
import de.araneaconsult.ig.rest.Constant.Match;
import de.araneaconsult.codegen.ig.rest.ApiException;
import de.araneaconsult.codegen.ig.rest.Configuration;
import de.araneaconsult.codegen.ig.rest.JSON;
import de.araneaconsult.codegen.ig.rest.api.DataPermsApi;
import de.araneaconsult.codegen.ig.rest.api.PolicyBroleApi;
import de.araneaconsult.codegen.ig.rest.model.Britem;
import de.araneaconsult.codegen.ig.rest.model.Burole;
import de.araneaconsult.codegen.ig.rest.model.Criteria;
import de.araneaconsult.codegen.ig.rest.model.Permission;
import de.araneaconsult.codegen.ig.rest.model.Permissions;
import de.araneaconsult.codegen.ig.rest.model.Roles;
import de.araneaconsult.codegen.ig.rest.model.SearchCriteria;
import de.araneaconsult.codegen.ig.rest.model.UniquenessCriterion;
import de.araneaconsult.codegen.ig.rest.model.Britem.AutoRequestEnum;

public class Test {

    public void run() throws ApiException, IOException, ResponseException {
        String broleName = "Gonna see what happens";
        
        JSON json = new JSON();

        OAuth oAuth = new OAuth(TestEnv.UA_URL, TestEnv.UA_OSP_CLIENTID, TestEnv.UA_OSP_SECRET);
        Token token = oAuth.requestToken(TestEnv.UA_USER, TestEnv.UA_PASS);

        Configuration.getDefaultApiClient().addDefaultHeader("Authorization", token.getAuthHeader());

        PolicyBroleApi broleApi = new PolicyBroleApi();

        SearchCriteria searchCriteria = new SearchCriteria();

        Criteria criteria = new Criteria();
        criteria.setOperator("AND");

        UniquenessCriterion uniquenessCriterion = new UniquenessCriterion();
        uniquenessCriterion.setAttributeKey("name");
        uniquenessCriterion.setOperator("EQ");
        uniquenessCriterion.setValue(broleName);
        criteria.addChildCriteriaItem(uniquenessCriterion);

        searchCriteria.addCriteriasItem(json.serialize(criteria));


        Roles roles = broleApi.getBusinessRolePolicies(searchCriteria, null, null, null, null, null, null, null, null, null, null, null, null, null);
        Burole burole = new Burole();
        if (roles.getArraySize() == 1) {
            burole = roles.getRoles().get(0);
        } else {
            burole = new Burole();
            burole.setDescription("My (not so very) first REST BRole");
            burole.setName(broleName);
            burole = broleApi.createBusinessRole(burole);
        }

        System.out.println(burole.getId());

        DataPermsApi dataPermsApi = new DataPermsApi();
        searchCriteria = new SearchCriteria();

        criteria = new Criteria();
        criteria.setOperator("AND");

        uniquenessCriterion = new UniquenessCriterion();
        uniquenessCriterion.setAttributeKey("name");
        uniquenessCriterion.setOperator("EQ");
        uniquenessCriterion.setValue(TestEnv.PERMISSION);
        criteria.addChildCriteriaItem(uniquenessCriterion);

        searchCriteria.addCriteriasItem(json.serialize(criteria));

        Permissions r = dataPermsApi.getPermissionsDataAdvancedFilter(searchCriteria, 2, null, null, null, 0, null, Match.EXACT.getValue(), null, true, null, true, null);
        Permission permission = r.getPermissions().get(0);
        System.out.println(permission.getPermId());

        Britem britem = new Britem();
        criteria = new Criteria();
        criteria.setOperator("AND");
        uniquenessCriterion = new UniquenessCriterion();
        uniquenessCriterion.setAttributeKey("department");
        uniquenessCriterion.operator("EQ");
        uniquenessCriterion.addValuesItem(TestEnv.DEPARTMENT);
        criteria.addChildCriteriaItem(uniquenessCriterion);
        britem.setCriteria(json.serialize(criteria));
        //britem.setType("CRITERIA");
        burole.addMembCriteriasItem(britem);

        britem = new Britem();
        britem.setType("PERMISSION");
        britem.setUniqueId(permission.getUniquePermId());
        britem.autoRequest(AutoRequestEnum.AUTO_GRANT_AND_REVOKE);
        britem.setMandatory(true);
        burole.addAuthorizedPermsItem(britem);

        burole = broleApi.updateBusinessRole(burole, burole.getId(), false);

        //burole = broleApi.deleteBusinessRole(burole.getId());
        System.out.println(burole.getId());
    }

    public static void main(String[] args) {
        try {
            Test t = new Test();
            t.run();
        } catch (ApiException ae) {
            System.err.println(ae.getCode() + ": " + ae.getResponseBody());
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
