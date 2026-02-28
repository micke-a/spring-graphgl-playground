<#import "layout/base.ftlh" as layout>

<@layout.page title="Customers">

<div x-data="{
    customers: [],
    loading: true,
    error: null,
    async init() {
        try {
            const data = await graphqlFetch('{ getAllCustomers { id name greeting } }');
            this.customers = data.getAllCustomers || [];
        } catch (e) { this.error = e.message; }
        finally { this.loading = false; }
    }
}">

    <h1 class="mb-4">Customers</h1>

    <template x-if="loading">
        <div class="text-center py-5">
            <div class="spinner-border" role="status">
                <span class="visually-hidden">Loading...</span>
            </div>
        </div>
    </template>

    <template x-if="error">
        <div class="alert alert-danger" x-text="error"></div>
    </template>

    <template x-if="!loading && !error">
        <div>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Greeting</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                    <template x-for="customer in customers" :key="customer.id">
                        <tr>
                            <td x-text="customer.id"></td>
                            <td x-text="customer.name"></td>
                            <td x-text="customer.greeting"></td>
                            <td>
                                <a :href="'/customers/' + customer.id" class="btn btn-sm btn-outline-primary">View</a>
                            </td>
                        </tr>
                    </template>
                </tbody>
            </table>
        </div>
    </template>

</div>

</@layout.page>
