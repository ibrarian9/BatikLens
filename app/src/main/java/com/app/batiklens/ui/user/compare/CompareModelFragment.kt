package com.app.batiklens.ui.user.compare

import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.batiklens.R
import com.app.batiklens.adapters.DataLatihAdapter
import com.app.batiklens.databinding.FragmentCompareModelBinding
import com.app.batiklens.di.Injection.messageToast
import com.app.batiklens.di.models.DataLatih
import com.app.batiklens.di.models.PredictionResult
import com.app.batiklens.helper.ImageClassifierHelper
import com.app.batiklens.ui.user.detailCompare.DetailCompareActivity
import org.tensorflow.lite.task.vision.classifier.Classifications

class CompareModelFragment : Fragment() {

    private var _binding: FragmentCompareModelBinding? = null
    private val bind get() = _binding!!
    private val dataLatihAdapter = DataLatihAdapter()
    private val selectedModels = mutableListOf<String>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind.apply {
            listDataLatih.layoutManager = GridLayoutManager(requireActivity(), 2)
            listDataLatih.addItemDecoration(SpaceItemDecoration(10))
            listDataLatih.adapter = dataLatihAdapter

            val dataListMotif = resources.getStringArray(R.array.data_name)
            val dataListMotifImage = resources.obtainTypedArray(R.array.data_image)

            val dataLatih = mutableListOf<DataLatih>()
            for (i in dataListMotif.indices){
                val drawableResId = dataListMotifImage.getResourceId(i, -1)
                val item = DataLatih(
                    namaMotif = dataListMotif[i],
                    linkImage = drawableResId
                )
                dataLatih.add(item)
            }
            dataListMotifImage.recycle()

            dataLatihAdapter.submitList(dataLatih)

            predict.setOnClickListener {
                val selectedItem = dataLatihAdapter.getSelectedItem()
                if (selectedItem == null) {
                    messageToast(requireActivity(), "Tidak ada motif batik yang dipilih!")
                    return@setOnClickListener
                }

                if (selectedModels.isEmpty()) {
                    messageToast(requireActivity(), "Tidak ada model yang dipilih!")
                    return@setOnClickListener
                }

                compareModels(selectedItem.linkImage, selectedItem.namaMotif)
            }

            buttonToggleGroup.addOnButtonCheckedListener{ _, checkedId, isChecked ->
                val modulName = when(checkedId) {
                    R.id.inceptionBtn -> "BatikLens_InceptionV3_Model_Metadata.tflite"
                    R.id.mobilenetButton -> "BatikLens_Model_with_Metadata.tflite"
                    R.id.xceptionBtn -> "BatikLens_Xception_Model_Metadata.tflite"
                    else -> null
                }
                if (modulName != null) {
                    if (isChecked) selectedModels.add(modulName) else selectedModels.remove(modulName)
                }
            }
        }
    }

    private fun compareModels(linkImage: Int, namaMotif: String) {
        val uri = Uri.parse("android.resource://${requireActivity().packageName}/$linkImage")

        val modelResults = mutableListOf<PredictionResult>()

        val models = mapOf(
            "Inception" to "BatikLens_InceptionV3_Model_Metadata.tflite",
            "MobileNet" to "BatikLens_Model_with_Metadata.tflite",
            "Xception" to "BatikLens_Xception_Model_Metadata.tflite"
        )

        val selectModulName = models.filter { it.value in selectedModels }

        if (selectModulName.isEmpty()) {
            messageToast(requireActivity(), "Tidak ada model yang dipilih!")
            return
        }

        for ((name, moduleName) in selectModulName) {
            val classifier = ImageClassifierHelper(
                context = requireActivity(),
                modulName = moduleName,
                classifierListener = object : ImageClassifierHelper.ClassifierListener {
                    override fun onErr(err: String) {
                        messageToast(requireActivity(), "$name Error : $err")
                    }

                    override fun onResult(result: List<Classifications>?, inferenceTime: Double) {
                        result?.let { classifications ->
                            val topCategory = classifications.firstOrNull()?.categories?.maxByOrNull { it.score }

                            if (topCategory != null) {
                                val confidenceScore = (topCategory.score * 100).toString()
                                val predictionResult = PredictionResult(
                                    nameModul = name,
                                    time = inferenceTime,
                                    confidance = confidenceScore
                                )

                                modelResults.add(predictionResult)

                                if (modelResults.size == selectModulName.size) {
                                    displayComparisonResults(modelResults, linkImage, namaMotif)
                                }
                            } else {
                                messageToast(requireActivity(), "Tidak ditemukan kategori dalam hasil klasifikasi untuk model $name")
                            }
                        }
                    }
                }
            )
            classifier.classifyStaticImage(uri)
        }
    }

    private fun displayComparisonResults(
        results: MutableList<PredictionResult>,
        linkImage: Int,
        namaMotif: String
    ) {
        // Sort by inference time (faster to slower)
        val sortedByTime = results.sortedBy { it.time }
        val fastest = sortedByTime.first()
        val slowest = sortedByTime.last()

        // Sort by confidence (higher to lower)
        val sortedByConfidence = results.sortedByDescending { it.confidance.toDoubleOrNull() ?: 0.0 }
        val highestConfidence = sortedByConfidence.first()
        val lowestConfidence = sortedByConfidence.last()

        val summary = StringBuilder()
        summary.append("Model Tercepat : ${fastest.nameModul} (${fastest.time}s)\n\n")
        summary.append("Model Terlambat : ${slowest.nameModul} (${slowest.time}s)\n\n")
        summary.append("Skor Akurasi Tertinggi : ${highestConfidence.nameModul} (${highestConfidence.confidance}%)\n\n")
        summary.append("Skor Akurasi Terendah : ${lowestConfidence.nameModul} (${lowestConfidence.confidance}%)\n")


        val i = Intent(requireActivity(), DetailCompareActivity::class.java).apply {
            putParcelableArrayListExtra(DetailCompareActivity.RESULT_COMPARE, ArrayList(results))
            putExtra(DetailCompareActivity.RESULT_FAST, summary.toString())
            putExtra(DetailCompareActivity.NAME_MOTIF, namaMotif)
            putExtra(DetailCompareActivity.IMAGE_URI, linkImage)
        }
        startActivity(i)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompareModelBinding.inflate(inflater, container, false)
        return bind.root
    }

    class SpaceItemDecoration(private val space: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val position = parent.getChildAdapterPosition(view) // Get item position
            val spanCount = (parent.layoutManager as? GridLayoutManager)?.spanCount ?: 1

            // Apply spacing logic for GridLayoutManager
            outRect.left = space / 2
            outRect.right = space / 2
            outRect.top = if (position < spanCount) space else space / 2
            outRect.bottom = space / 2
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}