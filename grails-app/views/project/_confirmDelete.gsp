<div id="confirmDeleteProject" title="Delete Project" class="showInModel">
    <p>Are you sure you want to delete ${project.name}.</p>
    <g:link action="delete" controller="project" class="modelLink" id="${project.id}">Delete</g:link>
    <input type="button" value="Cancel" class="closeModel">
</div>