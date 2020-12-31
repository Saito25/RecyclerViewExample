package es.manuel.recyclerviewexample.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.*
import com.google.android.material.snackbar.Snackbar
import es.manuel.recyclerviewexample.R
import es.manuel.recyclerviewexample.databinding.ActivityMainBinding
import es.manuel.recyclerviewexample.model.local.database.Repository
import es.manuel.recyclerviewexample.model.local.entity.Student
import es.manuel.recyclerviewexample.utils.doOnSwipe
import es.manuel.recyclerviewexample.utils.observeEvent

class ActivityMain : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel: ActivityMainViewModel by viewModels {
        ActivityMainViewModelFactory(Repository, application, this)
    }

    private val listAdapter = ActivityMainAdapter().apply {
        setOnItemClickListener { showStudent(currentList[it]) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViews()
        observeLiveData()
        observeEvents()
    }

    private fun setupViews() {
        setupRecyclerView()
        binding.btnAddStudent.setOnClickListener {
            viewModel.addStudent(Student(0, "No name", 18))
        }
        binding.btnDeleteAllStudent.setOnClickListener {
            viewModel.deleteAllUser()
        }
        binding.btnFilterByLegalAge.setOnClickListener {
            viewModel.filterStudents()
        }
    }

    private fun observeLiveData() {
        viewModel.students.observe(this) {
            updateList(it)
        }
        viewModel.emptyViewStatus.observe(this) {
            binding.txtMainEmptyView.visibility = it
        }
        viewModel.filterText.observe(this) {
            binding.btnFilterByLegalAge.text = it
        }
    }

    private fun observeEvents() {
        viewModel.onEmptyListMessage.observeEvent(this) {
            showNoStudentToDeleteMessage(it)
        }
    }

    private fun setupRecyclerView() {
        binding.rcvMain.run {
            setHasFixedSize(true)
            adapter = listAdapter
            layoutManager = LinearLayoutManager(this@ActivityMain)
            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(this@ActivityMain, RecyclerView.VERTICAL))
            doOnSwipe { viewHolder, _ ->
                deleteStudent(listAdapter.currentList[viewHolder.adapterPosition])
            }
        }
    }

    private fun showStudent(student: Student) {
        Snackbar.make(binding.root, student.name, Snackbar.LENGTH_SHORT).show()
    }

    private fun deleteStudent(student: Student) {
        val deleted = viewModel.deleteStudent(student)
        if (deleted) {
            showUndo(student)
        } else {
            showNoDeleted(student)
        }
    }

    private fun updateList(newList: List<Student>) {
        listAdapter.submitList(newList)
        /*binding.txtMainEmptyView.visibility =
            if (newList.isEmpty()) View.VISIBLE else View.INVISIBLE*/
    }

    private fun showUndo(student: Student) {
        Snackbar.make(binding.root, getString(R.string.deleted_user, student.name), Snackbar.LENGTH_LONG)
            .setAction(R.string.main_undo) {
                viewModel.addStudent(student)
            }
            .show()
    }

    private fun showNoDeleted(student: Student) {
        val studentPosition = viewModel.notifyChange(student)
        Snackbar.make(binding.root, "It was not possible delete to ${student.name}", Snackbar.LENGTH_LONG)
            .show()
        listAdapter.notifyItemChanged(studentPosition)
    }

    private fun showNoStudentToDeleteMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG).show()
    }
}