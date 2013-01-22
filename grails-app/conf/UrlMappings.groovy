class UrlMappings {

	static mappings = {
		
		// allow the type to act as a path param into a query param to
		// have RESTful style links for the transactions
		// ie: /transaction/bill/show/1
		//     /transaction/deposit/list
		"/transaction/$type?/$action?/$id?" {
			controller = 'transaction';
		}
		
		// define the standard URL format
		"/$controller/$action?/$id?" { }
		
		// define the default action
		"/"(controller:'overview', action:'list')
		
		// handle error pages
		"500"(view:'/error')
	}
}
