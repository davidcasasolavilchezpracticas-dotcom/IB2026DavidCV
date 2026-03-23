package com.iberdrola.practicas2026.davidcv.data.local.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.iberdrola.practicas2026.davidcv.data.local.database.BillDatabase
import com.iberdrola.practicas2026.davidcv.data.local.entity.BillEntity
import com.iberdrola.practicas2026.davidcv.data.local.entity.ContractEntity
import com.iberdrola.practicas2026.davidcv.domain.model.bill.BillType
import com.iberdrola.practicas2026.davidcv.domain.model.bill.PaymentStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractStatus
import com.iberdrola.practicas2026.davidcv.domain.model.contract.ContractType
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class DaoTest {

    private lateinit var db: BillDatabase
    private lateinit var billDao: BillDao
    private lateinit var contractDao: ContractDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BillDatabase::class.java).build()
        billDao = db.billDao()
        contractDao = db.contractDao()
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertAndReadBill() = runBlocking {
        val bill = BillEntity(
            id = 1,
            type = BillType.LIGHT,
            value = 45.0f,
            startDate = LocalDateTime.now(),
            endDate = LocalDateTime.now(),
            paymentStatus = PaymentStatus.PAID
        )
        billDao.insert(bill)
        val allBills = billDao.getAll().first()
        assertEquals(1, allBills.size)
        assertEquals(bill.id, allBills[0].id)
    }

    @Test
    fun insertAndReadContract() = runBlocking {
        val contract = ContractEntity(
            id = 1,
            type = ContractType.GAS,
            status = ContractStatus.ACTIVE,
            phone = "123456789",
            email = "test@test.com"
        )
        contractDao.insertAll(listOf(contract))
        val allContracts = contractDao.getAll().first()
        assertEquals(1, allContracts.size)
        assertEquals(contract.id, allContracts[0].id)
    }

    @Test
    fun updateContractEmail() = runBlocking {
        val contract = ContractEntity(
            id = 1,
            type = ContractType.LIGHT,
            status = ContractStatus.ACTIVE,
            email = "old@email.com"
        )
        contractDao.insertAll(listOf(contract))
        
        val newEmail = "new@email.com"
        contractDao.updateEmail(1, newEmail)
        
        val updatedContract = contractDao.getAll().first()[0]
        assertEquals(newEmail, updatedContract.email)
    }
}
