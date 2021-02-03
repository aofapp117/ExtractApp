import numpy as np
from pdfminer.pdfinterp import PDFResourceManager,PDFPageInterpreter
from pdfminer.converter import TextConverter
from pdfminer.layout import LAParams
from pdfminer.pdfpage import PDFPage
from io import BytesIO


def pdf_to_text(path):
    manager = PDFResourceManager()
    restr = BytesIO()
    layout = LAParams(all_texts = True)
    device = TextConverter(manager, restr, laparams = layout)
    filepath = open(path, 'rb')
    interpreter = PDFPageInterpreter(manager, device)
    for page in PDFPage.get_pages(filepath, check_extractable = True):
        interpreter.process_page(page)
    text = restr.getvalue()
    filepath.close()
    device.close()
    restr.close()
    return text

def text_split(file):
    try :
        TextOutput = pdf_to_text(file)
        text_utf = TextOutput.decode("utf-8")
        texts = text_utf.split()
        return texts
    except Exception as e :
        return str(e)

def find_snnid_(file):
    subject_list = []
    s_id = []
    s_name = []
    subject = ""
    TextOutput = pdf_to_text(file)
    text_utf = TextOutput.decode("utf-8")
    text = text_utf.split()
    for i in range(len(text)):
        if text[i].isnumeric() and len(text[i]) == 9 and text[i+1].isalpha() :
            s_id.append(text[i])
            # s_id += text[i] + ","
            for n in text[i+1:]:
                if n.isupper() and n.isalpha() :

                    subject += n + " "
                    continue
                else:
                    if  subject == "" :
                        break
                    else:
                        s_name.append(subject)
                        subject = ""
                        break



    #     s_id = list(set(s_id))
    subject_list.append(s_id)
    subject_list.append(s_name)
    arr = np.array(subject_list)
    #     s_id += s_name
    return subject_list



def helloworld(j):
    a = np.ones((j,2))
    return a