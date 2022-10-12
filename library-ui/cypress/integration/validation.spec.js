describe("Data validation in modals test", () => {
  it("Visits page", () => {
    cy.visit("http://localhost:4200/")
  })
  it("Checks validation in dialog for adding books", () => {
    cy.intercept('GET', '/api/books/available', { fixture: 'available-books.json' })
    cy.get("a[routerLink='/books/available'").click()
    cy.get(".btn-primary").click()
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(1).type("aa", { force: true }).then((v) => {
      cy.get(".form-control").eq(1).should("have.class", "is-invalid")
      cy.get(".form-control").eq(1).type("bb", { force: true }).then((v) => {
        cy.get(".form-control").eq(1).should("have.class", "is-valid")
        cy.get(".form-control")
          .eq(1)
          .type("123456789012345678901234567890123456789012345678901234567890", {
            force: true,
          }).then((v) => {
            cy.get(".form-control").eq(1).should("have.class", "is-invalid")
            cy.get(".form-control").eq(2).should("have.class", "is-invalid")
            cy.get(".form-control").eq(2).type("aa", { force: true }).then((v) => {
              cy.get(".form-control").eq(2).should("have.class", "is-invalid")
              cy.get(".form-control").eq(2).type("bb", { force: true }).then((v) => {
                cy.get(".form-control").eq(2).should("have.class", "is-valid")
                cy.get(".form-control")
                  .eq(2)
                  .type("123456789012345678901234567890123456789012345678901234567890", {
                    force: true,
                  }).then((v) => {
                    cy.get(".form-control").eq(2).should("have.class", "is-invalid")
                    cy.get(".close").click()
                  })
              })
            })
          })
      })
    })
  })
  it("Checks validation in dialog for lending books", () => {
    cy.get("input[name='searchTerm']").type("cyAuthor")
    cy.contains("Found 1")
    cy.get("i[ngbTooltip='Lend']").click()
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.contains("First name size")
    cy.contains("Last name size")
    cy.get(".modal-footer>button").should("be.disabled")
    cy.get(".form-control").eq(1).type("a bbb", { force: true }).then((v) => {
      cy.get(".form-control").eq(1).should("have.class", "is-invalid")
      cy.contains("First name size")
      cy.get(".modal-footer>button").should("be.disabled")
      cy.get(".form-control").eq(1).clear().type("aaa b", { force: true }).then((v) => {
        cy.get(".form-control").eq(1).should("have.class", "is-invalid")
        cy.contains("Last name size")
        cy.get(".modal-footer>button").should("be.disabled")
        cy.get(".form-control").eq(1).clear().type("aaa bbbb", { force: true }).then((v) => {
          cy.get(".form-control").eq(1).should("have.class", "is-valid")
          cy.get(".form-control")
            .eq(1)
            .clear()
            .type("123456789012345678901234567890123456789012345678901 aaa", {
              force: true,
            })
          cy.get(".form-control").eq(1).should("have.class", "is-invalid")
          cy.contains("First name size")
          cy.get(".modal-footer>button").should("be.disabled")
          cy.get(".form-control")
            .eq(1)
            .clear()
            .type("aaa 123456789012345678901234567890123456789012345678901", {
              force: true,
            })
          cy.get(".form-control").eq(1).should("have.class", "is-invalid")
          cy.contains("Last name size")
          cy.get(".modal-footer>button").should("be.disabled")
          cy.get(".form-control")
            .eq(1)
            .clear()
            .type(
              "123456789012345678901234567890123456789012345678901 123456789012345678901234567890123456789012345678901",
              { force: true }
            )
          cy.get(".form-control").eq(1).should("have.class", "is-invalid")
          cy.contains("First name size")
          cy.contains("Last name size")
          cy.get(".modal-footer>button").should("be.disabled")
          cy.get(".form-control").eq(3).should("have.class", "is-valid")
          cy.get(".form-control").eq(3).type("aa", { force: true })
          cy.get(".form-control").eq(3).should("have.class", "is-valid")
          cy.get(".form-control").eq(3).type("bb", { force: true })
          cy.get(".form-control").eq(3).should("have.class", "is-valid")
          cy.get(".form-control")
            .eq(3)
            .type("123456789012345678901234567890123456789012345678901234567890", {
              force: true,
            })
          cy.get(".form-control").eq(3).should("have.class", "is-invalid")
          cy.get(".close").click()
        })
      })
    })
  })
  it("Checks validation in dialog for returning books", () => {
    cy.intercept('GET', '/api/books/lent', { fixture: 'lent-books.json' })
    cy.get("a[routerLink='/books/lent'").click()
    cy.get("input[name='searchTerm']").type("cyAuthor")
    cy.contains("Found 1")
    cy.get("i[ngbTooltip='Return']").click()
    cy.get("input[placeholder='yyyy-mm-dd']").should("have.class", "is-valid")
    cy.get(".ti-calendar").click()
    cy.get("select[title='Select year']").select("2019")
    cy.get(".btn-light").eq(0).click()
    cy.get("input[placeholder='yyyy-mm-dd']").should("have.class", "is-invalid")
    cy.contains("Book can't be returned before 2021-02-21")
    cy.get(".ti-calendar").click()
    cy.get("select[title='Select year']").select("2022")
    cy.get(".btn-light").eq(0).click()
    cy.get("input[placeholder='yyyy-mm-dd']").should("have.class", "is-valid")
    cy.get(".close").click()
  })
  it("Checks validation in dialog for adding categories", () => {
    cy.get("a[routerLink='/categories'").click()
    cy.get(".btn-primary").click()
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(1).type("aa", { force: true })
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(1).type("bb", { force: true })
    cy.get(".form-control").eq(1).should("have.class", "is-valid")
    cy.get(".form-control")
      .eq(1)
      .type("123456789012345678901234567890123456789012345678901234567890", {
        force: true,
      })
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".close").click()
  })
  it("Checks validation in dialog for adding borrowers", () => {
    cy.get("a[routerLink='/borrowers'").click()
    cy.get(".btn-primary").click()
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(1).type("aa", { force: true })
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(1).type("bb", { force: true })
    cy.get(".form-control").eq(1).should("have.class", "is-valid")
    cy.get(".form-control")
      .eq(1)
      .type("123456789012345678901234567890123456789012345678901234567890", {
        force: true,
      })
    cy.get(".form-control").eq(1).should("have.class", "is-invalid")
    cy.get(".form-control").eq(2).should("have.class", "is-invalid")
    cy.get(".form-control").eq(2).type("aa", { force: true })
    cy.get(".form-control").eq(2).should("have.class", "is-invalid")
    cy.get(".form-control").eq(2).type("bb", { force: true })
    cy.get(".form-control").eq(2).should("have.class", "is-valid")
    cy.get(".form-control")
      .eq(2)
      .type("123456789012345678901234567890123456789012345678901234567890", {
        force: true,
      })
    cy.get(".form-control").eq(2).should("have.class", "is-invalid")
    cy.get(".close").click()
  })
})
