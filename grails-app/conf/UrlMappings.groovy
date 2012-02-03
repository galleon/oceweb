class UrlMappings {

    static mappings = {
        "/$controller/$action?/$id?" {
            constraints {
                // apply constraints here
            }
        }
        "/$name" {
            controller = "project"
            action = "index"
        }

        "/" {
            controller = "project"
            action = "index"
        }
        "500"(view: '/error')
    }
}
