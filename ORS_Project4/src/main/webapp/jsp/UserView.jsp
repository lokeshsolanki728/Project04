diff
--- a/ORS_Project4/src/main/webapp/jsp/UserView.jsp
+++ b/ORS_Project4/src/main/webapp/jsp/UserView.jsp
@@ -19,7 +19,7 @@
   <script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
   <script>
   $( function() {
-    $( "#udatee" ).datepicker({
+    $( "#dob" ).datepicker({
       changeMonth: true,
       changeYear: true,
       yearRange: '1980:2002',
@@ -75,7 +75,7 @@
 					<th align="left"><label for="login">Login Id <span class="required">*</span>:</label></th>
 					<td><input type="text" name="login" id="login" class="form-control"
 						placeholder="Enter EmailId" value="${bean.login}"
-						<c:if test="${bean.id > 0}">readonly</c:if>></td>
+						<c:if test="${bean.id > 0}">readonly</c:if> <c:if test="${bean.id == 0}"></c:if></td>
 					<td><div class="error-message">${requestScope.login}</div></td>
 				</tr>
 				<c:choose>
@@ -133,8 +133,9 @@
 				<tr>
 					<th align="left"><label for="roleId">Role <span class="required">*</span>:</label></th>
 					<td><select class="form-control" name="roleId" id="roleId">
+						<option value="">Select Role</option>
 					<c:forEach items="${roleList}" var="role">
-					 <option value="${role.key}" ${role.key == bean.roleId ? 'selected' : ''}>${role.value}</option>
+					 <option value="${role.key}" ${role.key == bean.roleId ? 'selected' : ''} ${role.key == param.roleId ? 'selected' : ''}>${role.value}</option>
                     </c:forEach>
                     </select></td>
 					<td><div class="error-message">${requestScope.roleId}</div></td>
@@ -143,7 +144,7 @@
 					<th style="padding: 3px"></th>
 				</tr>
 				<tr>
-					<th align="left"><label for="udatee">Date Of Birth <span class="required">*</span>:</label></th>
+					<th align="left"><label for="dob">Date Of Birth <span class="required">*</span>:</label></th>
 					<td><input type="text" name="dob" id="udatee" class="form-control"
 						placeholder="Enter Date Of Birth" readonly="readonly"
 						value="${bean.dob}"></td>