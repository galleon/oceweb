@artifact.package@import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields = true, excludes = 'dateCreated,lastUpdated,metaClass')
@EqualsAndHashCode

class @artifact.name@ {
/* Dependency Injections (e.g. Services) */

/* Fields */
Date dateCreated
Date lastUpdated

/* Transients */

/* Relations */

/* Constraints */
static constraints = {
}

/* Mappings */
static mapping = {
}

/* Hooks */

/* Named queries */

/* Transient Methods */

/* Methods */

/* Static Methods */
}

