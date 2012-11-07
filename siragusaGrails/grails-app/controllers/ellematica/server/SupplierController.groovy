package ellematica.server

class SupplierController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 30, 1000)
				if(!params.sort)params.sort="name"
        [supplierInstanceList: Supplier.list(params), supplierInstanceTotal: Supplier.count()]
    }

    def create = {
        def supplierInstance = new Supplier()
        supplierInstance.properties = params
        return [supplierInstance: supplierInstance]
    }

    def save = {
        def supplierInstance = new Supplier(params)
        if (supplierInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'supplier.label', default: 'Supplier'), supplierInstance.id])}"
            redirect(action: "show", id: supplierInstance.id)
        }
        else {
            render(view: "create", model: [supplierInstance: supplierInstance])
        }
    }

    def show = {
        def supplierInstance = Supplier.get(params.id)
        if (!supplierInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")
        }
        else {
            [supplierInstance: supplierInstance]
        }
    }

    def edit = {
        def supplierInstance = Supplier.get(params.id)
        if (!supplierInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [supplierInstance: supplierInstance]
        }
    }

    def update = {
        def supplierInstance = Supplier.get(params.id)
        if (supplierInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (supplierInstance.version > version) {
                    
                    supplierInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'supplier.label', default: 'Supplier')] as Object[], "Another user has updated this Supplier while you were editing")
                    render(view: "edit", model: [supplierInstance: supplierInstance])
                    return
                }
            }
            supplierInstance.properties = params
            if (!supplierInstance.hasErrors() && supplierInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'supplier.label', default: 'Supplier'), supplierInstance.id])}"
                redirect(action: "show", id: supplierInstance.id)
            }
            else {
                render(view: "edit", model: [supplierInstance: supplierInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def supplierInstance = Supplier.get(params.id)
        if (supplierInstance) {
            try {
							Commissione.findAllBySupplier(supplierInstance).each{ it.delete(flush:true)}
                supplierInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'supplier.label', default: 'Supplier'), params.id])}"
            redirect(action: "list")
        }
    }
	
	def prova = {}
}
