<fieldset class="form">
    <div class="data-fields">
        <g:render template="shapeInfo"/>
        <div class="fieldcontain required">
            <label for="topRadius">
                Radius
                <span class="required-indicator">*</span>
            </label>
            <g:textField name="topRadius" value=""/>
        </div>

        <div class="fieldcontain required">
            <label for="height">
                Height
                <span class="required-indicator">*</span>
            </label>
            <g:textField name="height" value=""/>
        </div>
    </div>
</fieldset>
<fieldset class="buttons">
    <input type="button" name="create" class="save" value="Create"/>
</fieldset>